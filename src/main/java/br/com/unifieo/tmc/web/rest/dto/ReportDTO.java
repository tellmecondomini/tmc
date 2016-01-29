package br.com.unifieo.tmc.web.rest.dto;

import org.joda.time.DateTime;

public class ReportDTO {

    private DateTime dataInicio;
    private DateTime dataFim;

    public ReportDTO() {
        this.dataInicio = new DateTime();
        this.dataFim = new DateTime();
    }

    public ReportDTO(DateTime dataInicio, DateTime dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public DateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(DateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public DateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(DateTime dataFim) {
        this.dataFim = dataFim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportDTO reportDTO = (ReportDTO) o;

        if (dataInicio != null ? !dataInicio.equals(reportDTO.dataInicio) : reportDTO.dataInicio != null) return false;
        return dataFim != null ? dataFim.equals(reportDTO.dataFim) : reportDTO.dataFim == null;

    }

    @Override
    public int hashCode() {
        int result = dataInicio != null ? dataInicio.hashCode() : 0;
        result = 31 * result + (dataFim != null ? dataFim.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "ReportDTO{" +
            "dataInicio=" + dataInicio +
            ", dataFim=" + dataFim +
            '}';
    }
}
