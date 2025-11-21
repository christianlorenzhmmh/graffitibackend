const express = require('express');
const cors = require('cors');
const multer = require('multer');
const path = require('path');
const fs = require('fs');
require('dotenv').config();

const pool = require('./db');

const app = express();
const PORT = process.env.PORT || 3000;
const UPLOAD_DIR = process.env.UPLOAD_DIR || 'uploads';

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Create uploads directory if it doesn't exist
if (!fs.existsSync(UPLOAD_DIR)) {
  fs.mkdirSync(UPLOAD_DIR, { recursive: true });
}

// Configure multer for file uploads
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, UPLOAD_DIR);
  },
  filename: function (req, file, cb) {
    const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
    cb(null, uniqueSuffix + path.extname(file.originalname));
  }
});

const upload = multer({
  storage: storage,
  limits: {
    fileSize: 10 * 1024 * 1024 // 10MB limit
  },
  fileFilter: function (req, file, cb) {
    const allowedTypes = /jpeg|jpg|png|gif|webp/;
    const extname = allowedTypes.test(path.extname(file.originalname).toLowerCase());
    const mimetype = allowedTypes.test(file.mimetype);

    if (mimetype && extname) {
      return cb(null, true);
    } else {
      cb(new Error('Only image files are allowed!'));
    }
  }
});

// Serve uploaded files
app.use('/uploads', express.static(UPLOAD_DIR));

// Health check endpoint
app.get('/api/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

// POST /api/graffiti - Create new graffiti entry with photo
app.post('/api/graffiti', upload.single('photo'), async (req, res) => {
  try {
    const { title, description, location, latitude, longitude, status } = req.body;

    if (!title) {
      return res.status(400).json({ error: 'Title is required' });
    }

    const photoPath = req.file ? `/uploads/${req.file.filename}` : null;

    const result = await pool.query(
      `INSERT INTO graffiti (title, description, location, latitude, longitude, photo_path, status)
       VALUES ($1, $2, $3, $4, $5, $6, $7)
       RETURNING *`,
      [
        title,
        description || null,
        location || null,
        latitude ? parseFloat(latitude) : null,
        longitude ? parseFloat(longitude) : null,
        photoPath,
        status || 'reported'
      ]
    );

    res.status(201).json({
      success: true,
      data: result.rows[0]
    });
  } catch (error) {
    console.error('Error creating graffiti entry:', error);
    
    // Clean up uploaded file if database insert fails
    if (req.file) {
      fs.unlink(path.join(UPLOAD_DIR, req.file.filename), (err) => {
        if (err) console.error('Error deleting file:', err);
      });
    }
    
    res.status(500).json({ error: 'Failed to create graffiti entry' });
  }
});

// GET /api/graffiti - List all graffiti entries
app.get('/api/graffiti', async (req, res) => {
  try {
    const { status, limit = 100, offset = 0 } = req.query;

    let query = 'SELECT * FROM graffiti';
    const params = [];

    if (status) {
      query += ' WHERE status = $1';
      params.push(status);
    }

    query += ' ORDER BY created_at DESC LIMIT $' + (params.length + 1) + ' OFFSET $' + (params.length + 2);
    params.push(parseInt(limit), parseInt(offset));

    const result = await pool.query(query, params);

    res.json({
      success: true,
      count: result.rows.length,
      data: result.rows
    });
  } catch (error) {
    console.error('Error fetching graffiti entries:', error);
    res.status(500).json({ error: 'Failed to fetch graffiti entries' });
  }
});

// GET /api/graffiti/:id - Get single graffiti entry
app.get('/api/graffiti/:id', async (req, res) => {
  try {
    const { id } = req.params;

    const result = await pool.query('SELECT * FROM graffiti WHERE id = $1', [id]);

    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Graffiti entry not found' });
    }

    res.json({
      success: true,
      data: result.rows[0]
    });
  } catch (error) {
    console.error('Error fetching graffiti entry:', error);
    res.status(500).json({ error: 'Failed to fetch graffiti entry' });
  }
});

// PUT /api/graffiti/:id - Update graffiti entry
app.put('/api/graffiti/:id', upload.single('photo'), async (req, res) => {
  try {
    const { id } = req.params;
    const { title, description, location, latitude, longitude, status } = req.body;

    // Check if entry exists
    const existingEntry = await pool.query('SELECT * FROM graffiti WHERE id = $1', [id]);
    
    if (existingEntry.rows.length === 0) {
      if (req.file) {
        fs.unlink(path.join(UPLOAD_DIR, req.file.filename), (err) => {
          if (err) console.error('Error deleting file:', err);
        });
      }
      return res.status(404).json({ error: 'Graffiti entry not found' });
    }

    // Build update query dynamically
    const updates = [];
    const values = [];
    let paramIndex = 1;

    if (title !== undefined) {
      updates.push(`title = $${paramIndex++}`);
      values.push(title);
    }
    if (description !== undefined) {
      updates.push(`description = $${paramIndex++}`);
      values.push(description);
    }
    if (location !== undefined) {
      updates.push(`location = $${paramIndex++}`);
      values.push(location);
    }
    if (latitude !== undefined) {
      updates.push(`latitude = $${paramIndex++}`);
      values.push(latitude ? parseFloat(latitude) : null);
    }
    if (longitude !== undefined) {
      updates.push(`longitude = $${paramIndex++}`);
      values.push(longitude ? parseFloat(longitude) : null);
    }
    if (status !== undefined) {
      updates.push(`status = $${paramIndex++}`);
      values.push(status);
    }

    // Handle new photo upload
    if (req.file) {
      const photoPath = `/uploads/${req.file.filename}`;
      updates.push(`photo_path = $${paramIndex++}`);
      values.push(photoPath);

      // Delete old photo if it exists
      const oldPhotoPath = existingEntry.rows[0].photo_path;
      if (oldPhotoPath) {
        const oldFileName = oldPhotoPath.replace('/uploads/', '');
        fs.unlink(path.join(UPLOAD_DIR, oldFileName), (err) => {
          if (err) console.error('Error deleting old file:', err);
        });
      }
    }

    if (updates.length === 0) {
      return res.status(400).json({ error: 'No fields to update' });
    }

    updates.push(`updated_at = CURRENT_TIMESTAMP`);
    values.push(id);

    const query = `UPDATE graffiti SET ${updates.join(', ')} WHERE id = $${paramIndex} RETURNING *`;
    const result = await pool.query(query, values);

    res.json({
      success: true,
      data: result.rows[0]
    });
  } catch (error) {
    console.error('Error updating graffiti entry:', error);
    
    if (req.file) {
      fs.unlink(path.join(UPLOAD_DIR, req.file.filename), (err) => {
        if (err) console.error('Error deleting file:', err);
      });
    }
    
    res.status(500).json({ error: 'Failed to update graffiti entry' });
  }
});

// DELETE /api/graffiti/:id - Delete graffiti entry
app.delete('/api/graffiti/:id', async (req, res) => {
  try {
    const { id } = req.params;

    // Get the entry to find the photo path
    const result = await pool.query('SELECT * FROM graffiti WHERE id = $1', [id]);

    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Graffiti entry not found' });
    }

    const entry = result.rows[0];

    // Delete from database
    await pool.query('DELETE FROM graffiti WHERE id = $1', [id]);

    // Delete photo file if it exists
    if (entry.photo_path) {
      const fileName = entry.photo_path.replace('/uploads/', '');
      const filePath = path.join(UPLOAD_DIR, fileName);
      
      fs.unlink(filePath, (err) => {
        if (err) console.error('Error deleting file:', err);
      });
    }

    res.json({
      success: true,
      message: 'Graffiti entry deleted successfully'
    });
  } catch (error) {
    console.error('Error deleting graffiti entry:', error);
    res.status(500).json({ error: 'Failed to delete graffiti entry' });
  }
});

// Error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  
  if (err instanceof multer.MulterError) {
    if (err.code === 'LIMIT_FILE_SIZE') {
      return res.status(400).json({ error: 'File size too large. Maximum size is 10MB' });
    }
    return res.status(400).json({ error: err.message });
  }
  
  res.status(500).json({ error: err.message || 'Something went wrong!' });
});

// Start server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
  console.log(`Upload directory: ${UPLOAD_DIR}`);
});

module.exports = app;
