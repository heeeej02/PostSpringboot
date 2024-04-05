package com.ddps.post.service;

import com.ddps.post.dto.PostDto;
import com.ddps.post.entity.Post;
import com.ddps.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Transactional
    public void savePost(PostDto postDto){
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .createDate(postDto.getCreateDate())
                .updateDate(postDto.getUpdateDate())
                .build();
        postRepository.save(post);
    }

    @Transactional
    public List<PostDto> getAllPosts(){
        return postRepository.findAll().stream()
                .map(post -> new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getCreateDate(), post.getUpdateDate()))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto getPostById(Long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getCreateDate(), post.getUpdateDate());
        } else {
            throw new IllegalArgumentException("해당 id의 게시물을 찾을 수 없습니다: " + id);
        }
    }

    @Transactional
    public void updatePost(PostDto postDto){
        Optional<Post> optionalPost = postRepository.findById(postDto.getId());
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            post.setTitle(postDto.getTitle()); // 제목을 업데이트
            post.setContent(postDto.getContent());
            post.setUpdateDate(postDto.getUpdateDate());
            postRepository.save(post); // 업데이트된 게시물을 저장
        } else {
            throw new IllegalArgumentException("해당 id의 게시물을 찾을 수 없습니다: " + postDto.getId());
        }
    }

    @Transactional
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }
    // 삭제 여부를 확인하는 메소드 추가
    public boolean isPostDeleted(Long id) {
        return !postRepository.existsById(id);
    }
}
