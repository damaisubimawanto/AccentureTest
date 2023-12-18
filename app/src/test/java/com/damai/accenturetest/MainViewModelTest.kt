package com.damai.accenturetest

import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.damai.accenturetest.room.AppDatabase
import com.damai.accenturetest.ui.MainViewModel
import com.damai.base.testutils.CoroutineTestRule
import com.damai.base.testutils.InstantExecutorExtension
import com.damai.base.utils.Event
import com.damai.base.utils.UserSharedPreferencesHelper
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToRemoteKeyEntityMapper
import com.damai.data.mappers.UserDetailsResponseToUserEntityMapper
import com.damai.data.mappers.UserEntityToUserDetailsModelMapper
import com.damai.data.mappers.UserFavoriteEntityToUserDetailsModelMapper
import com.damai.domain.daos.UserFavoriteDao
import com.damai.domain.entities.UserFavoriteEntity
import com.damai.domain.models.UserDetailsModel
import com.jraska.livedata.test
import io.mockk.confirmVerified
import io.mockk.excludeRecords
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch

/**
 * Created by damai007 on 18/December/2023
 */
@RunWith(AndroidJUnit4::class)
@ExtendWith(InstantExecutorExtension::class)
@MediumTest
@Config(manifest = Config.NONE)
class MainViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var database: AppDatabase
    private lateinit var userFavoriteDao: UserFavoriteDao
    private lateinit var viewModel: MainViewModel

    //region Mockk Variables
    private val mainService = mockk<MainService>()
    private val userSharedPreferencesHelper = mockk<UserSharedPreferencesHelper>()
    private val userDetailsToUserEntityMapper = mockk<UserDetailsResponseToUserEntityMapper>()
    private val userDetailsToRemoteKeyEntityMapper = mockk<UserDetailsResponseToRemoteKeyEntityMapper>()
    private val userEntityToUserDetailsModelMapper = mockk<UserEntityToUserDetailsModelMapper>()
    private val userFavoriteEntityToUserDetailsModelMapper = mockk<UserFavoriteEntityToUserDetailsModelMapper>()

    private val userClickedObserver = mockk<Observer<Event<Pair<Int, String>>>>(relaxed = true)
    private val userSearchObserver = mockk<Observer<Event<String>>>(relaxed = true)
    private val userFavoriteListObserver = mockk<Observer<List<UserDetailsModel>>>(relaxed = true)
    //endregion `Mockk Variables`

    //region Getter Variables
    private val userId get() = 10
    private val username get() = "jokosampurna"
    private val searchText get() = "cat"
    //endregion `Getter Variables`

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        userFavoriteDao = database.userFavoriteDao()

        viewModel = MainViewModel(
            mainService = mainService,
            database = database,
            dispatcherProvider = coroutineRule.dispatcherProvider,
            userSharedPreferencesHelper = userSharedPreferencesHelper,
            userDetailsToUserEntityMapper = userDetailsToUserEntityMapper,
            userDetailsToRemoteKeyEntityMapper = userDetailsToRemoteKeyEntityMapper,
            userEntityToUserDetailsModelMapper = userEntityToUserDetailsModelMapper,
            userFavoriteEntityToUserDetailsModelMapper = userFavoriteEntityToUserDetailsModelMapper
        )
        viewModel.userClickedLiveData.observeForever(userClickedObserver)
        viewModel.userSearchLiveData.observeForever(userSearchObserver)
        viewModel.userFavoriteListLiveData.observeForever(userFavoriteListObserver)
    }

    @After
    fun cleanUp() {
        viewModel.userClickedLiveData.removeObserver(userClickedObserver)
        viewModel.userSearchLiveData.removeObserver(userSearchObserver)
        viewModel.userFavoriteListLiveData.removeObserver(userFavoriteListObserver)
        database.close()
    }

    @Test
    fun `(+) trigger user click should update live data`() = runTest {
        viewModel.triggerUserClick(
            userId = userId,
            username = username
        )

        verify(exactly = 1) {
            userClickedObserver.onChanged(any())
        }

        val testObserver = viewModel.userClickedLiveData.test()
            .assertHasValue()
        val content = testObserver.value().peekContent()
        assertEquals(userId, content.first)
        assertEquals(username, content.second)

        excludeRecords {
            viewModel.userClickedLiveData.observeForever(testObserver)
        }
        confirmVerified(userClickedObserver)
    }

    @Test
    fun `(+) trigger user search should update live data`() = runTest {
        viewModel.triggerUserSearch(searchText = searchText)

        verify(exactly = 1) {
            userSearchObserver.onChanged(any())
        }

        val testObserver = viewModel.userSearchLiveData.test()
            .assertHasValue()
        val content = testObserver.value().peekContent()
        assertEquals(searchText, content)

        excludeRecords {
            viewModel.userSearchLiveData.observeForever(testObserver)
        }
        confirmVerified(userSearchObserver)
    }

    @Test
    fun `(+) insert new user favorite into database should be success`() = runTest {
        val userFavoriteEntity = UserFavoriteEntity(
            username = username,
            id = userId,
            avatarUrl = null,
            url = null,
            htmlUrl = null,
            name = null,
            company = null,
            blog = null,
            location = null,
            email = null,
            bio = null,
            twitterUsername = null,
            publicRepos = 0,
            publicGists = 0,
            followers = 0,
            following = 0,
            createdAt = "",
            updatedAt = ""
        )
        userFavoriteDao.insert(user = userFavoriteEntity)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            val savedUserFavoriteList = userFavoriteDao.getAllUserFavorites()
            assertThat(savedUserFavoriteList).isNotEmpty
            assertTrue(savedUserFavoriteList.first().id == userId)
            assertTrue(savedUserFavoriteList.first().username == username)
            latch.countDown()
        }
        latch.await()
        job.cancelAndJoin()
    }
}