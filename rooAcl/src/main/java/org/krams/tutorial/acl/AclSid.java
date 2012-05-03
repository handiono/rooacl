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
@Table(name = "acl_sid", uniqueConstraints = { 
												@UniqueConstraint(columnNames = {
																					"sid", "principal" 
																				}
																 )
											 }
)
@RooJson
public class AclSid {

    @NotNull
    private Boolean principal;

    @NotNull
    private String sid;


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static AclSid fromJsonToAclSid(String json) {
        return new JSONDeserializer<AclSid>().use(null, AclSid.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AclSid> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<AclSid> fromJsonArrayToAclSids(String json) {
        return new JSONDeserializer<List<AclSid>>().use(null, ArrayList.class).use("values", AclSid.class).deserialize(json);
    }

	public Boolean getPrincipal() {
        return this.principal;
    }

	public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

	public String getSid() {
        return this.sid;
    }

	public void setSid(String sid) {
        this.sid = sid;
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new AclSid().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAclSids() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AclSid o", Long.class).getSingleResult();
    }

	public static List<AclSid> findAllAclSids() {
        return entityManager().createQuery("SELECT o FROM AclSid o", AclSid.class).getResultList();
    }

	public static AclSid findAclSid(Long id) {
        if (id == null) return null;
        return entityManager().find(AclSid.class, id);
    }

	public static List<AclSid> findAclSidEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AclSid o", AclSid.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AclSid attached = AclSid.findAclSid(this.id);
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
    public AclSid merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AclSid merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
