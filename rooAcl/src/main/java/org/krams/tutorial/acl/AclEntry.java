package org.krams.tutorial.acl;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord(versionField = "")
@RooJson
@Table(name = "acl_entry",  uniqueConstraints = { 
											@UniqueConstraint(columnNames = {
																				"acl_object_identity", 
																				"ace_order" 
																			}) 
											    }
)
public class AclEntry {

	@ManyToOne
    @JoinColumn(name = "acl_object_identity", referencedColumnName = "id", nullable = false)
    private AclObjectIdentity aclObjectIdentity;

	@ManyToOne
    @JoinColumn(name = "sid", referencedColumnName = "id", nullable = false)
    private AclSid sid;

	@Column(name = "ace_order")
    @NotNull
    private Integer aceOrder;

    @NotNull
    private Integer mask;

    @NotNull
    private Boolean granting;

    @NotNull
    private Boolean auditSuccess;

    @NotNull
    private Boolean auditFailure;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public AclObjectIdentity getAclObjectIdentity() {
        return this.aclObjectIdentity;
    }

	public void setAclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
        this.aclObjectIdentity = aclObjectIdentity;
    }

	public AclSid getSid() {
        return this.sid;
    }

	public void setSid(AclSid sid) {
        this.sid = sid;
    }

	public Integer getAceOrder() {
        return this.aceOrder;
    }

	public void setAceOrder(Integer aceOrder) {
        this.aceOrder = aceOrder;
    }

	public Integer getMask() {
        return this.mask;
    }

	public void setMask(Integer mask) {
        this.mask = mask;
    }

	public Boolean getGranting() {
        return this.granting;
    }

	public void setGranting(Boolean granting) {
        this.granting = granting;
    }

	public Boolean getAuditSuccess() {
        return this.auditSuccess;
    }

	public void setAuditSuccess(Boolean auditSuccess) {
        this.auditSuccess = auditSuccess;
    }

	public Boolean getAuditFailure() {
        return this.auditFailure;
    }

	public void setAuditFailure(Boolean auditFailure) {
        this.auditFailure = auditFailure;
    }

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static AclEntry fromJsonToAclEntry(String json) {
        return new JSONDeserializer<AclEntry>().use(null, AclEntry.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AclEntry> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<AclEntry> fromJsonArrayToAclEntrys(String json) {
        return new JSONDeserializer<List<AclEntry>>().use(null, ArrayList.class).use("values", AclEntry.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new AclEntry().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAclEntrys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AclEntry o", Long.class).getSingleResult();
    }

	public static List<AclEntry> findAllAclEntrys() {
        return entityManager().createQuery("SELECT o FROM AclEntry o", AclEntry.class).getResultList();
    }

	public static AclEntry findAclEntry(Long id) {
        if (id == null) return null;
        return entityManager().find(AclEntry.class, id);
    }

	public static List<AclEntry> findAclEntryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AclEntry o", AclEntry.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AclEntry attached = AclEntry.findAclEntry(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public AclEntry merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AclEntry merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
