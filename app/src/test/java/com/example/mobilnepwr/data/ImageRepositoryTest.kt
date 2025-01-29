package com.example.mobilnepwr.data

import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.images.ImageDao
import com.example.mobilnepwr.data.images.ImageRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ImageRepositoryTest {
    private val imageDao = mockk<ImageDao>()
    private lateinit var imageRepository: ImageRepository

    @Before
    fun setUp() {
        imageRepository = ImageRepository(imageDao)
    }

    @Test
    fun `getAllItemsStream should return flow of images`() = runTest {
        val images = listOf(
            Image(
                imageId = 1,
                courseId = 1,
                filePath = "path1"
            ),
            Image(
                imageId = 2,
                courseId = 1,
                filePath = "path2"
            )
        )

        every { imageDao.getAllImages() } returns flowOf(images)

        imageRepository.getAllItemsStream().collect { result ->
            assert(result == images)
        }
        coVerify {
            imageDao.getAllImages()
        }
    }

    @Test
    fun `getImageByCourseId should return flow of images`() = runTest {
        val images = listOf(
            Image(
                imageId = 1,
                courseId = 1,
                filePath = "path1"
            ),
            Image(
                imageId = 2,
                courseId = 1,
                filePath = "path2"
            )
        )
        every { imageDao.getImageByCourseId(1) } returns flowOf(images)

        imageRepository.getImageByCourseId(1).collect { result ->
            assert(result == images)
        }
        coVerify {
            imageDao.getImageByCourseId(1)
        }
    }

    @Test
    fun `getItemStream should return flow of image`() = runTest {
        val image = Image(
            imageId = 1,
            courseId = 1,
            filePath = "path1"
        )
        every { imageDao.getImage(1) } returns flowOf(image)
        imageRepository.getItemStream(1).collect { result ->
            assert(result == image)
        }
        coVerify {
            imageDao.getImage(1)
        }
    }

    @Test
    fun `insertItem should call imageDao insert`() = runTest {
        val image = Image(
            imageId = 1,
            courseId = 1,
            filePath = "path1"
        )
        coEvery { imageDao.insertImage(image) } just runs
        imageRepository.insertItem(image)
        coVerify {
            imageDao.insertImage(image)
        }

    }

    @Test
    fun `deleteItem should call imageDao delete`() = runTest {
        val image = Image(
            imageId = 1,
            courseId = 1,
            filePath = "path1"
        )
        coEvery { imageDao.deleteImage(image) } just runs
        imageRepository.deleteItem(image)
        coVerify {
            imageDao.deleteImage(image)
        }
    }


}