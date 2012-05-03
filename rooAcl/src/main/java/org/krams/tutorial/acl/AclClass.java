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
@Table(name = "acl_class")
@RooJson
public class AclClass {

	@Column(name = "class", length = 255)
    @NotNull
    private String clazz;


	public String getClazz() {
        return this.clazz;
    }

	public void setClazz(String clazz) {
        this.clazz = clazz;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
        EntityManager em = new AclClass().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAclClasses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AclClass o", Long.class).getSingleResult();
    }

	public static List<AclClass> findAllAclClasses() {
        return entityManager().createQuery("SELECT o FROM AclClass o", AclClass.class).getResultList();
    }

	public static AclClass findAclClass(Long id) {
        if (id == null) return null;
        return entityManager().find(AclClass.class, id);
    }

	public static List<AclClass> findAclClassEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AclClass o", AclClass.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AclClass attached = AclClass.findAclClass(this.id);
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
    public AclClass merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AclClass merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static AclClass fromJsonToAclClass(String json) {
        return new JSONDeserializer<AclClass>().use(null, AclClass.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AclClass> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<AclClass> fromJsonArrayToAclClasses(String json) {
        return new JSONDeserializer<List<AclClass>>().use(null, ArrayList.class).use("values", AclClass.class).deserialize(json);
    }
}
