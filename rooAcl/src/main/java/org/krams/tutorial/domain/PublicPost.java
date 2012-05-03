package org.krams.tutorial.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(schema = "public",name = "public_post")
@RooJavaBean
@RooToString
@RooJpaActiveRecord(versionField = "", table = "public_post", schema = "public")
@RooJson
public class PublicPost {

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date date;

    @NotNull
    private String message;


	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static PublicPost fromJsonToPublicPost(String json) {
        return new JSONDeserializer<PublicPost>().use(null, PublicPost.class).deserialize(json);
    }

	public static String toJsonArray(Collection<PublicPost> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<PublicPost> fromJsonArrayToPublicPosts(String json) {
        return new JSONDeserializer<List<PublicPost>>().use(null, ArrayList.class).use("values", PublicPost.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Date getDate() {
        return this.date;
    }

	public void setDate(Date date) {
        this.date = date;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new PublicPost().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPublicPosts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PublicPost o", Long.class).getSingleResult();
    }

	public static List<PublicPost> findAllPublicPosts() {
        return entityManager().createQuery("SELECT o FROM PublicPost o", PublicPost.class).getResultList();
    }

	public static PublicPost findPublicPost(Long id) {
        if (id == null) return null;
        return entityManager().find(PublicPost.class, id);
    }

	public static List<PublicPost> findPublicPostEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PublicPost o", PublicPost.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            PublicPost attached = PublicPost.findPublicPost(this.id);
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
    public PublicPost merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PublicPost merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
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
}
