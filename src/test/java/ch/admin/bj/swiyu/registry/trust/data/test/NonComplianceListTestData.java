package ch.admin.bj.swiyu.registry.trust.data.test;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NonComplianceListTestData {

    public static final String NON_COMPLIANCE_LIST_PAYLOAD_1 =
        """
        {
          "nonCompliantActors": [
            {
              "did": "did:tdw:alpha123",
              "flaggedAsNonCompliantAt": "2025-10-21T12:52:40.372037Z",
              "reason": {
                "de": "",
                "en": "Violation of policy",
                "it": "",
                "rm": "",
                "fr": ""
              }
            },
            {
              "did": "did:tdw:alpha234",
              "flaggedAsNonCompliantAt": "2025-10-21T12:53:07.044848Z",
              "reason": {
                "de": "Regelverstoss",
                "en": "",
                "it": "",
                "rm": "",
                "fr": ""
              }
            },
            {
              "did": "did:tdw:alpha345",
              "flaggedAsNonCompliantAt": "2025-10-21T12:53:48.478818Z",
              "reason": {
                "de": "Beispielgrund",
                "en": "",
                "it": "",
                "rm": "",
                "fr": ""
              }
            },
            {
              "did": "did:tdw:alpha456",
              "flaggedAsNonCompliantAt": "2025-10-21T12:54:56.357542Z",
              "reason": {
                "de": "Beispielgrund 2",
                "en": "",
                "it": "",
                "rm": "",
                "fr": ""
              }
            }
          ]
        }
        """;

    public static final String NON_COMPLIANCE_LIST_PAYLOAD_2 =
        """
        {
          "nonCompliantActors": [
            {
              "did": "did:tdw:alpha123",
              "flaggedAsNonCompliantAt": "2025-10-21T12:52:40.372037Z",
              "reason": {
                "de": "",
                "en": "Violation of policy",
                "it": "",
                "rm": "",
                "fr": ""
              }
            },
            {
              "did": "did:tdw:alpha234",
              "flaggedAsNonCompliantAt": "2025-10-21T12:53:07.044848Z",
              "reason": {
                "de": "Regelverstoss",
                "en": "",
                "it": "",
                "rm": "",
                "fr": ""
              }
            },
            {
              "did": "did:tdw:alpha345",
              "flaggedAsNonCompliantAt": "2025-10-21T12:53:48.478818Z",
              "reason": {
                "de": "Beispielgrund",
                "en": "",
                "it": "",
                "rm": "",
                "fr": ""
              }
            },
            {
              "did": "did:tdw:alpha456",
              "flaggedAsNonCompliantAt": "2025-10-21T12:54:56.357542Z",
              "reason": {
                "de": "Beispielgrund 2",
                "en": "",
                "it": "",
                "rm": "",
                "fr": ""
              }
            },
            {
              "did": "did:tdw:alpha567",
              "flaggedAsNonCompliantAt": "2025-10-21T12:58:14.471924Z",
              "reason": {
                "de": "Beispielgrund 3",
                "en": "",
                "it": "",
                "rm": "",
                "fr": ""
              }
            }
          ]
        }
        """;
}
