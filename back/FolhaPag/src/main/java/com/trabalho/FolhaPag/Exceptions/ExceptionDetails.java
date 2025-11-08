package com.trabalho.FolhaPag.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionDetails {

    private String message;
    private LocalDate date;
    private String issuer;
}
