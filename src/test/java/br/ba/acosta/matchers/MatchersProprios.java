package br.ba.acosta.matchers;

import java.util.Calendar;

public class MatchersProprios {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiNumaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DataDiferencaDiaMatcher ehHojeComDiferencaDias(Integer qtdDias) {
        return new DataDiferencaDiaMatcher(qtdDias);
    }

    public static DataDiferencaDiaMatcher ehHoje() {
        return new DataDiferencaDiaMatcher(0);
    }

}
