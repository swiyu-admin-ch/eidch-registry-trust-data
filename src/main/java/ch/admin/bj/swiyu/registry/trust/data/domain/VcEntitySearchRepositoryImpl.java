package ch.admin.bj.swiyu.registry.trust.data.domain;

import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public class VcEntitySearchRepositoryImpl implements VcEntitySearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VcEntity> search(TrustStatementFilter filter) {
        var sql = new StringBuilder(
            """
                SELECT ts.* FROM {h-schema}vc_entity ts
                JOIN {h-schema}datastore_entity de ON de.id = ts.base_id
                WHERE 1=1
            """
        );

        if (hasText(filter.getDid())) {
            sql.append(" AND ts.vc_payload->>'sub' = :did");
        }
        if (filter.getMaxNotBefore() != null) {
            sql.append(" AND (ts.vc_payload->>'nbf')::bigint <= :maxNotBefore");
        }
        if (filter.getMinExpiration() != null) {
            sql.append(" AND (ts.vc_payload->>'exp')::bigint >= :minExpiration");
        }
        if (!isEmpty(filter.getVct())) {
            sql.append(" AND ts.vc_payload->>'vct' IN (:vct)");
        }
        if (hasText(filter.getCanIssue())) {
            sql.append(" AND ts.vc_payload->>'canIssue' IN (:canIssue)");
        }
        if (hasText(filter.getCanVerify())) {
            sql.append(" AND ts.vc_payload->>'canVerify' IN (:canVerify)");
        }
        if (filter.getStatus() != null) {
            sql.append(" AND de.status = :status");
        }

        var query = entityManager.createNativeQuery(sql.toString(), VcEntity.class);
        if (hasText(filter.getDid())) {
            query.setParameter("did", filter.getDid());
        }
        if (filter.getMaxNotBefore() != null) {
            query.setParameter("maxNotBefore", filter.getMaxNotBefore().getEpochSecond());
        }
        if (filter.getMinExpiration() != null) {
            query.setParameter("minExpiration", filter.getMinExpiration().getEpochSecond());
        }
        if (filter.getStatus() != null) {
            query.setParameter("status", filter.getStatus().toString());
        }
        if (!isEmpty(filter.getVct())) {
            query.setParameter("vct", filter.getVct());
        }
        if (hasText(filter.getCanIssue())) {
            query.setParameter("canIssue", filter.getCanIssue());
        }
        if (hasText(filter.getCanVerify())) {
            query.setParameter("canVerify", filter.getCanVerify());
        }
        return query.getResultList();
    }
}
