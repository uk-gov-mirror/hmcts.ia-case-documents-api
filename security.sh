#!/usr/bin/env bash
echo ${TEST_URL}
echo 'testing create file' > test_create.txt
cat test_create.txt
echo '{
  "site" : "https://ia-case-documents-api-aat.service.core-compute-aat.internal/v2/api-docs",
  "issues" : [
    {
      "id" : "90022",
      "name" : "Application Error Disclosure",
      "state" : "inprogress",
      "link": "https://tools.hmcts.net/jira/browse/RIA-989"
    }
  ]
}%' > security-ignore.conf
cat security-ignore.conf
zap-api-scan.py -t ${TEST_URL}/v2/api-docs -f openapi -P 1001 -p security-ignore.conf
cat zap.out
zap-cli --zap-url http://0.0.0.0 -p 1001 report -o /zap/api-report.html -f html
echo "listings of zap folder"
ls -la /zap
cp /zap/api-report.html functional-output/
zap-cli -p 1001 alerts -l Informational