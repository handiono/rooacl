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

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(versionField = "")
@RooJson
@Table(name = "acl_object_identity", uniqueConstraints = { 
															@UniqueConstraint(columnNames = {
																								"object_id_class", 
																								"object_id_identity" 
																							}) 
														 }
)
public class AclObjectIdentity {


	@ManyToOne
    @JoinColumn(name = "object_id_class", referencedColumnName = "id", nullable = false)
    private AclClass objectIdClass;

	@ManyToOne
    @JoinColumn(name = "owner_sid", referencedColumnName = "id", nullable = false)
    private AclSid ownerSid;

	@Column(name = "object_id_identity")
    @NotNull
    private Long objectIdIdentity;

    @ManyToOne
    @JoinColumn(name = "parent_object", referencedColumnName = "id", nullable = true)
    private AclObjectIdentity parentObject;

	@Column(name = "entries_inheriting")
    @NotNull
    private Boolean entriesInheriting;

	

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static AclObjectIdentity fromJsonToAclObjectIdentity(String json) {
        return new JSONDeserializer<AclObjectIdentity>().use(null, AclObjectIdentity.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AclObjectIdentity> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<AclObjectIdentity> fromJsonArrayToAclObjectIdentitys(String json) {
        return new JSONDeserializer<List<AclObjectIdentity>>().use(null, ArrayList.class).use("values", AclObjectIdentity.class).deserialize(json);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new AclObjectIdentity().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAclObjectIdentitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AclObjectIdentity o", Long.class).getSingleResult();
    }

	public static List<AclObjectIdentity> findAllAclObjectIdentitys() {
        return entityManager().createQuery("SELECT o FROM AclObjectIdentity o", AclObjectIdentity.class).getResultList();
    }

	public static AclObjectIdentity findAclObjectIdentity(Long id) {
        if (id == null) return null;
        return entityManager().find(AclObjectIdentity.class, id);
    }

	public static List<AclObjectIdentity> findAclObjectIdentityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AclObjectIdentity o", AclObjectIdentity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AclObjectIdentity attached = AclObjectIdentity.findAclObjectIdentity(this.id);
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
    public AclObjectIdentity merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AclObjectIdentity merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public AclClass getObjectIdClass() {
        return this.objectIdClass;
    }

	public void setObjectIdClass(AclClass objectIdClass) {
        this.objectIdClass = objectIdClass;
    }

	public AclSid getOwnerSid() {
        return this.ownerSid;
    }

	public void setOwnerSid(AclSid ownerSid) {
        this.ownerSid = ownerSid;
    }

	public Long getObjectIdIdentity() {
        return this.objectIdIdentity;
    }

	public void setObjectIdIdentity(Long objectIdIdentity) {
        this.objectIdIdentity = objectIdIdentity;
    }

	public AclObjectIdentity getParentObject() {
        return this.parentObject;
    }

	public void setParentObject(AclObjectIdentity parentObject) {
        this.parentObject = parentObject;
    }

	public Boolean getEntriesInheriting() {
        return this.entriesInheriting;
    }

	public void setEntriesInheriting(Boolean entriesInheriting) {
        this.entriesInheriting = entriesInheriting;
    }

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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
