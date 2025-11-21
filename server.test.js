const request = require('supertest');
const app = require('./server');
const pool = require('./db');
const fs = require('fs');
const path = require('path');

// Mock database pool
jest.mock('./db');

describe('Graffiti API Endpoints', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterAll(() => {
    jest.restoreAllMocks();
  });

  describe('GET /api/health', () => {
    it('should return health status', async () => {
      const response = await request(app)
        .get('/api/health')
        .expect(200);

      expect(response.body).toHaveProperty('status', 'ok');
      expect(response.body).toHaveProperty('timestamp');
    });
  });

  describe('POST /api/graffiti', () => {
    it('should create a graffiti entry without photo', async () => {
      const mockResult = {
        rows: [{
          id: 1,
          title: 'Test Graffiti',
          description: 'Test Description',
          location: 'Test Location',
          latitude: 40.7128,
          longitude: -74.0060,
          photo_path: null,
          status: 'reported',
          created_at: new Date(),
          updated_at: new Date()
        }]
      };

      pool.query.mockResolvedValue(mockResult);

      const response = await request(app)
        .post('/api/graffiti')
        .field('title', 'Test Graffiti')
        .field('description', 'Test Description')
        .field('location', 'Test Location')
        .field('latitude', '40.7128')
        .field('longitude', '-74.0060')
        .expect(201);

      expect(response.body.success).toBe(true);
      expect(response.body.data).toHaveProperty('id');
      expect(response.body.data.title).toBe('Test Graffiti');
    });

    it('should return error when title is missing', async () => {
      const response = await request(app)
        .post('/api/graffiti')
        .field('description', 'Test Description')
        .expect(400);

      expect(response.body).toHaveProperty('error', 'Title is required');
    });
  });

  describe('GET /api/graffiti', () => {
    it('should list all graffiti entries', async () => {
      const mockResult = {
        rows: [
          {
            id: 1,
            title: 'Graffiti 1',
            description: 'Description 1',
            location: 'Location 1',
            latitude: 40.7128,
            longitude: -74.0060,
            photo_path: '/uploads/photo1.jpg',
            status: 'reported',
            created_at: new Date(),
            updated_at: new Date()
          },
          {
            id: 2,
            title: 'Graffiti 2',
            description: 'Description 2',
            location: 'Location 2',
            latitude: 40.7129,
            longitude: -74.0061,
            photo_path: '/uploads/photo2.jpg',
            status: 'cleaned',
            created_at: new Date(),
            updated_at: new Date()
          }
        ]
      };

      pool.query.mockResolvedValue(mockResult);

      const response = await request(app)
        .get('/api/graffiti')
        .expect(200);

      expect(response.body.success).toBe(true);
      expect(response.body.count).toBe(2);
      expect(response.body.data).toHaveLength(2);
    });

    it('should filter by status', async () => {
      const mockResult = {
        rows: [{
          id: 1,
          title: 'Graffiti 1',
          status: 'reported',
          created_at: new Date(),
          updated_at: new Date()
        }]
      };

      pool.query.mockResolvedValue(mockResult);

      const response = await request(app)
        .get('/api/graffiti?status=reported')
        .expect(200);

      expect(response.body.success).toBe(true);
      expect(response.body.count).toBe(1);
    });
  });

  describe('GET /api/graffiti/:id', () => {
    it('should get a single graffiti entry', async () => {
      const mockResult = {
        rows: [{
          id: 1,
          title: 'Test Graffiti',
          description: 'Test Description',
          location: 'Test Location',
          latitude: 40.7128,
          longitude: -74.0060,
          photo_path: '/uploads/photo.jpg',
          status: 'reported',
          created_at: new Date(),
          updated_at: new Date()
        }]
      };

      pool.query.mockResolvedValue(mockResult);

      const response = await request(app)
        .get('/api/graffiti/1')
        .expect(200);

      expect(response.body.success).toBe(true);
      expect(response.body.data.id).toBe(1);
      expect(response.body.data.title).toBe('Test Graffiti');
    });

    it('should return 404 for non-existent entry', async () => {
      pool.query.mockResolvedValue({ rows: [] });

      const response = await request(app)
        .get('/api/graffiti/999')
        .expect(404);

      expect(response.body).toHaveProperty('error', 'Graffiti entry not found');
    });
  });

  describe('DELETE /api/graffiti/:id', () => {
    it('should delete a graffiti entry', async () => {
      const mockSelectResult = {
        rows: [{
          id: 1,
          title: 'Test Graffiti',
          photo_path: null
        }]
      };

      pool.query
        .mockResolvedValueOnce(mockSelectResult) // SELECT
        .mockResolvedValueOnce({ rowCount: 1 }); // DELETE

      const response = await request(app)
        .delete('/api/graffiti/1')
        .expect(200);

      expect(response.body.success).toBe(true);
      expect(response.body.message).toBe('Graffiti entry deleted successfully');
    });

    it('should return 404 for non-existent entry', async () => {
      pool.query.mockResolvedValue({ rows: [] });

      const response = await request(app)
        .delete('/api/graffiti/999')
        .expect(404);

      expect(response.body).toHaveProperty('error', 'Graffiti entry not found');
    });
  });
});
