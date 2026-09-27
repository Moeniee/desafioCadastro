package util;

import model.Constantes;
import model.SexoPet;
import model.TipoPet;

public class FormatadorUtil {

    public static String formatarTipo(TipoPet tipo) {
        return tipo == TipoPet.CACHORRO ? "Cachorro" : "Gato";
    }

    public static String formatarSexo(SexoPet sexo) {
        return sexo == SexoPet.MACHO ? "Macho" : "Femea";
    }

    public static String formatarIdade(double idade) {
        if (idade == Constantes.IDADE_NAO_INFORMADA) {
            return Constantes.NAO_INFORMADO;
        }
        if (idade < 1) {
            int meses = (int) Math.round(idade * 12);
            return meses + " meses";
        }
        return idade + " anos";
    }

    public static String formatarPeso(double peso) {
        if (peso == Constantes.PESO_NAO_INFORMADO) {
            return Constantes.NAO_INFORMADO;
        }
        return peso + "kg";
    }
}