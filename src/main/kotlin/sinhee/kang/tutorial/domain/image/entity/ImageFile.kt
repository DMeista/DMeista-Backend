package sinhee.kang.tutorial.domain.image.entity

import sinhee.kang.tutorial.domain.post.entity.Post
import javax.persistence.*

@Entity(name = "tbl_image")
class ImageFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    val post: Post,

    val fileName: String
)
