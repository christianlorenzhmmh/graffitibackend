package de.lorenzware.graffitibackend.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationDto {
    public String email;
    public String password;

}
