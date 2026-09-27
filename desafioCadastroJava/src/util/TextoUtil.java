package util;

import java.text.Normalizer;

public class TextoUtil {

    public static String normalizar(String texto){
        if (texto == null) return null;
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toLowerCase().trim();
    }
}
