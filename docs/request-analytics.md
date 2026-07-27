# Request analytics

Every completed HTTP request is recorded in the `request_analytics` table when
`ANALYTICS_ENABLED` is `true`. Raw records are deleted after 30 days by default.
Change the period with `ANALYTICS_RETENTION_DAYS`.

## Collected fields

- UTC timestamp, request identifier, HTTP method, path and query parameter names.
- Response status, processing time, scheme and host.
- Client IP, authenticated user id when already resolved by the request, user agent,
  referrer without query parameters, accepted language and content types.

Request and response bodies, query parameter values, passwords, cookies,
authorization headers, session tokens and OAuth parameters are never stored.

## Grafana query examples

Requests and average response time per hour:

```sql
SELECT
    date_trunc('hour', occurred_at) AS time,
    COUNT(*) AS requests,
    AVG(duration_ms) AS average_duration_ms
FROM request_analytics
WHERE occurred_at BETWEEN $__timeFrom() AND $__timeTo()
GROUP BY 1
ORDER BY 1;
```

Most visited pages:

```sql
SELECT path, COUNT(*) AS requests
FROM request_analytics
WHERE occurred_at BETWEEN $__timeFrom() AND $__timeTo()
  AND method = 'GET'
GROUP BY path
ORDER BY requests DESC
LIMIT 20;
```

HTTP errors:

```sql
SELECT status_code, path, COUNT(*) AS responses
FROM request_analytics
WHERE occurred_at BETWEEN $__timeFrom() AND $__timeTo()
  AND status_code >= 400
GROUP BY status_code, path
ORDER BY responses DESC;
```

## Privacy checklist before production

The IP address and the authenticated user id can be personal data. Before enabling
collection in production, document the purpose and legal basis, disclose the fields
and retention period in the website privacy notice, restrict database and Grafana
access, and define a process for access and deletion requests. Prefer aggregated or
anonymised data for long-term studies.
