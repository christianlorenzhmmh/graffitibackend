package de.lorenzware.graffitibackend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadGraffitiResponse {

    public final static int RESPONSE_CODE_OK = 0;
    public final static int RESPONSE_CODE_EMPTY = 1;
    public final static int RESPONSE_CODE_MORE_THAN_MAX = 2;

    private int responseCode;
    private List<GraffitiDto> graffitiList;
    private List<TagCountDto> tagCounts;

//    public LoadGraffitiResponse(int responseCode, List<GraffitiDto> graffitiList, List<TagCountDto> tagCounts) {
//        this.responseCode = responseCode;
//        this.graffitiList = graffitiList;
//        this.tagCounts = tagCounts;
//    }

    // Getter und Setter
}