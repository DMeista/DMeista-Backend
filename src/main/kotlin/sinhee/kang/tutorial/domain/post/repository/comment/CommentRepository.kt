package sinhee.kang.tutorial.domain.post.repository.comment

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.entity.comment.Comment

@Repository
interface CommentRepository : CrudRepository<Comment, Int>
