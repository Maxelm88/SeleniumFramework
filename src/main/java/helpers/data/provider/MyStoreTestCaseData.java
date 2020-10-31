package helpers.data.provider;

import helpers.Common;
import helpers.datebase.TestDataManager;
import helpers.datebase.TestDataSelect;
import helpers.datebase.dto.CustomTestDTO;
import helpers.datebase.request.CustomDataRequest;
import helpers.dictionary.ApplicationName;
import helpers.dictionary.Profile;
import helpers.reporter.ReportManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class MyStoreTestCaseData extends AbstractTestCaseData {

    private String user;

    public MyStoreTestCaseData(TestDataManager manager, String inputRst, ApplicationName appName, Profile prof) {
        CustomDataRequest req = TestDataSelect.getSelectTestData(appName.getDescription(), prof, inputRst);

        CustomTestDTO customTestDTO = manager.getCustomDataManager().getCustomTestDataWithParams(req);
        if (customTestDTO == null) {
            Common.reporter().logFail("Not find inputData");
        } else {
            setBaseField(customTestDTO);
            user = customTestDTO.getParam1();
        }
    }

    @Override
    public CustomTestDTO castToCustomTestDto() {
        return CustomTestDTO.builder()
                .lp(lp)
                .nazwaTestu(jiraTicket)
                .nazwaAplikacji(applicationName.getDescription())
                .env(profile.name())
                .creationDate(creationDate)
                .status(dataRowStatus)
                .etap(stage)
                .param1(user)
                .build();
    }

    @Override
    public void printDetailsInReporter(ReportManager reporter) {
        reporter.logPassIsNotNull("User", user);
    }
}
