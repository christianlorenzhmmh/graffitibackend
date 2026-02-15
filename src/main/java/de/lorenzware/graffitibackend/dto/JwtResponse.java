package de.lorenzware.graffitibackend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
    public String token;
    public String role;
    public Long userId;
}