#!/usr/bin/env bash
echo ${TEST_URL}
cat security-ignore.conf
zap-api-scan.py -t ${TEST_URL}/v2/api-docs -f openapi -P 1001 -z "-config zap.spider.exclude_from_scan=https://ia-case-documents-api-aat.service.core-compute-aat.internal/v2/api-docs"
cat zap.out
zap-cli --zap-url http://0.0.0.0 -p 1001 report -o /zap/api-report.html -f html
echo "listings of zap folder"
ls -la /zap
cp /zap/api-report.html functional-output/
zap-cli -p 1001 alerts -l Informational