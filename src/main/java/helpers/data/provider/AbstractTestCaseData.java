package helpers.data.provider;

import helpers.common.DateUtils;
import helpers.datebase.dto.CustomTestDTO;
import helpers.dictionary.ApplicationName;
import helpers.dictionary.DataRowStatus;
import helpers.dictionary.Profile;
import helpers.reporter.ReportManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public abstract class AbstractTestCaseData {

    protected int lp;
    protected String jiraTicket;
    protected String nazwaTestu;
    protected ApplicationName nazwaAplikacji;
    protected Profile profile;
    protected Date creationDate;
    protected String stage;
    protected DataRowStatus dataRowStatus;
    protected boolean sendToDb;
    protected boolean finalReport;
    protected boolean noResetInputTestData;

    public abstract CustomTestDTO castToCustomTestDto();

    public abstract void printDetailsInReporter(ReportManager reporter);

    public void printInReporter(ReportManager reporter) {
        reporter.logPass("Data", DateUtils.getCurrentDate());
        reporter.logPass("Nazwa testu", nazwaTestu);
        reporter.logPass("Aplikacja", nazwaAplikacji.getDescription());
        reporter.logPass("Środowisko", profile.name());
        reporter.logPassIsNotNull("Etap", stage);
    }

    protected void setBaseField(CustomTestDTO customTestDTO) {
        lp = customTestDTO.getLp();
        jiraTicket = customTestDTO.getNazwaTestu();
        nazwaAplikacji = ApplicationName.getEnum(customTestDTO.getNazwaAplikacji());
        profile = Profile.getEnum(customTestDTO.getEnv());
        creationDate = customTestDTO.getCreationDate();
        dataRowStatus = customTestDTO.getStatus();
        stage = customTestDTO.getEtap();
    }
}
