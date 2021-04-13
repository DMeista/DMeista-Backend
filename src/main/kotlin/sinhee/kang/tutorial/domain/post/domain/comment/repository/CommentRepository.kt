package sinhee.kang.tutorial.domain.post.domain.comment.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.domain.comment.Comment

@Repository
interface CommentRepository : CrudRepository<Comment, Int>
