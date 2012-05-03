package org.krams.tutorial.service;

import java.util.List;
import org.krams.tutorial.domain.PublicPost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PublicPostServiceImpl implements PublicPostService {

	public long countAllPublicPosts() {
        return PublicPost.countPublicPosts();
    }

	public void deletePublicPost(PublicPost publicPost) {
        publicPost.remove();
    }

	public PublicPost findPublicPost(Long id) {
        return PublicPost.findPublicPost(id);
    }

	public List<PublicPost> findAllPublicPosts() {
        return PublicPost.findAllPublicPosts();
    }

	public List<PublicPost> findPublicPostEntries(int firstResult, int maxResults) {
        return PublicPost.findPublicPostEntries(firstResult, maxResults);
    }

	public void savePublicPost(PublicPost publicPost) {
        publicPost.persist();
    }

	public PublicPost updatePublicPost(PublicPost publicPost) {
        return publicPost.merge();
    }
}
