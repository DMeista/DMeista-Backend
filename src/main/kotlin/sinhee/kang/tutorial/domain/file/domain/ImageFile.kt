package sinhee.kang.tutorial.domain.file.domain

import sinhee.kang.tutorial.domain.post.domain.post.Post
import javax.persistence.*

@Entity(name = "tbl_image")
data class ImageFile (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val imageId: Int = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "image")
        val post: Post,

        val fileName: String
)
