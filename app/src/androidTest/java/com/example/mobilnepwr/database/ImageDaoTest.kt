package com.example.mobilnepwr.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mobilnepwr.data.AppDatabase
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.images.ImageDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ImageDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: ImageDao

    private val testCourses = TestData.courses

    private val testImages = listOf(
        Image(
            imageId = 1,
            courseId = testCourses[0].courseId,
            filePath = "path1"
        ),
        Image(
            imageId = 2,
            courseId = testCourses[0].courseId,
            filePath = "path2"
        ),
        Image(
            imageId = 3,
            courseId = testCourses[1].courseId,
            filePath = "path3"
        )
    )

    @Before
    fun setup() = runTest {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.imageDao()

        database.courseDao().insertAll(testCourses)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertImage() = runBlocking {
        dao.insertImage(testImages[0])
        val image = dao.getImage(testImages[0].imageId).first()
        assertEquals(testImages[0], image)
        dao.insertImage(testImages[1])
        assertEquals(2, dao.getAllImages().first().size)

    }

    @Test
    fun updateImage() = runBlocking {
        insertImages(testImages)
        val updatedImage = testImages[0].copy(filePath = "updated")
        dao.updateImage(updatedImage)
        val image = dao.getImage(testImages[0].imageId).first()
        assertEquals(updatedImage, image)

    }

    @Test
    fun deleteImage() = runBlocking {
        insertImages(testImages)
        dao.deleteImage(testImages[0])
        assertEquals(2, dao.getAllImages().first().size)
        assertEquals(null, dao.getImage(testImages[0].imageId).first())
    }

    @Test
    fun getImage() = runBlocking {
        insertImages(testImages)
        val image = dao.getImage(testImages[0].imageId).first()
        assertEquals(testImages[0], image)
        val image2 = dao.getImage(testImages[1].imageId).first()
        assertEquals(testImages[1], image2)
    }

    @Test
    fun getAllImages() = runBlocking {
        insertImages(testImages)
        val images = dao.getAllImages().first()
        assertEquals(testImages, images)
    }

    @Test
    fun getImageByCourseId() = runBlocking {
        insertImages(testImages)
        val images = dao.getImageByCourseId(testCourses[0].courseId).first()
        assertEquals(2, images.size)
        assertEquals(testImages.subList(0, 2), images)
        val images2 = dao.getImageByCourseId(testCourses[1].courseId).first()
        assertEquals(1, images2.size)
        assertEquals(testImages.subList(2, 3), images2)

    }

    private suspend fun insertImages(images: List<Image>) {
        for (image in images) {
            dao.insertImage(image)
        }
    }
}