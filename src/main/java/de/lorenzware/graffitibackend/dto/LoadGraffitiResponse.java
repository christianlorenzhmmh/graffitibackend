package de.lorenzware.graffitibackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoadGraffitiResponse {

    public final static int RESPONSE_CODE_OK = 0;
    public final static int RESPONSE_CODE_EMPTY = 1;
    public final static int RESPONSE_CODE_MORE_THAN_MAX = 2;

    private int responseCode;
    private List<GraffitiDto> graffitiList;

    public LoadGraffitiResponse(int responseCode, List<GraffitiDto> graffitiList) {
        this.responseCode = responseCode;
        this.graffitiList = graffitiList;
    }

    // Getter und Setter
}