package org.krams.tutorial.service;

import java.util.List;
import org.krams.tutorial.domain.AdminPost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AdminPostServiceImpl implements AdminPostService {

	public long countAllAdminPosts() {
        return AdminPost.countAdminPosts();
    }

	public void deleteAdminPost(AdminPost adminPost) {
        adminPost.remove();
    }

	public AdminPost findAdminPost(Long id) {
        return AdminPost.findAdminPost(id);
    }

	public List<AdminPost> findAllAdminPosts() {
        return AdminPost.findAllAdminPosts();
    }

	public List<AdminPost> findAdminPostEntries(int firstResult, int maxResults) {
        return AdminPost.findAdminPostEntries(firstResult, maxResults);
    }

	public void saveAdminPost(AdminPost adminPost) {
        adminPost.persist();
    }

	public AdminPost updateAdminPost(AdminPost adminPost) {
        return adminPost.merge();
    }
}
