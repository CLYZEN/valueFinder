package com.ezen.valuefinder.service;

import java.util.concurrent.CompletableFuture;

public interface LikeService {
    void addLike(Long postNo, Long memberId);

    void deleteLike(Long postNo, Long memberId);

    boolean checkLike(Long postNo, Long memberId);

    CompletableFuture<Long> countLike(Long postNo);
}
