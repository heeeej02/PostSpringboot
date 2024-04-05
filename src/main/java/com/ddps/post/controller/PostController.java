package com.ddps.post.controller;

import com.ddps.post.dto.PostDto;
import com.ddps.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/list")
    public List<PostDto> list(){
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostDto detail(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @PostMapping("/new")
    public ResponseEntity<String> create(@RequestBody PostDto postDto){
        try {
            postService.savePost(postDto);
            return ResponseEntity.ok("게시글이 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 추가에 실패했습니다 ");
        }
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<String> edit(@PathVariable Long id, @RequestBody PostDto postDto){
        try {
            postDto.setId(id); // 요청 본문에서 받은 ID를 설정
            postService.updatePost(postDto); // 게시물 업데이트 실행
            return ResponseEntity.ok("게시물이 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 예외 발생 시 오류 응답 반환
        }
    }

//    @PutMapping("/edit/{id}")
//    public void edit(@PathVariable Long id, @RequestBody PostDto postDto){
//        postDto.setId(id);
//        postService.updatePost(postDto);
//    }

//    @DeleteMapping("/delete/{id}")
//    public void delete(@PathVariable Long id){
//        postService.deletePost(id);
//    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        postService.deletePost(id);
        if(postService.isPostDeleted(id)) {
            return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("게시물 삭제에 실패했습니다.");
        }
    }
}
