package br.ba.acosta.matchers;

import br.ba.acosta.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataDiferencaDiaMatcher extends TypeSafeMatcher<Date> {
    private Integer qtdDias;

    public DataDiferencaDiaMatcher(Integer qtdDias) {
        this.qtdDias = qtdDias;
    }

    @Override
    public void describeTo(Description description) {
        Date dataEsperada = DataUtils.obterDataComDiferencaDias(qtdDias);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        description.appendText(dateFormat.format(dataEsperada));
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(qtdDias));
    }
}
