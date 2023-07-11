package com.damai.data.repos

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.damai.base.utils.Constants.HEADER_LINK_NAME
import com.damai.base.utils.Constants.NEXT_PATTERN
import com.damai.base.utils.Constants.QUERY_PARAM_SINCE
import com.damai.data.apiservices.MainService
import com.damai.data.mappers.UserDetailsResponseToUserDetailsModelMapper
import com.damai.domain.models.UserDetailsModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Created by damai007 on 11/July/2023
 */
class UserDetailsListPagingSource @Inject constructor(
    private val mainService: MainService,
    private val userDetailsMapper: UserDetailsResponseToUserDetailsModelMapper
) : PagingSource<Int, UserDetailsModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserDetailsModel> {
        val loadResult = try {
            val response = mainService.getUserListAsync(params.key).await()
            val headers = response.headers()
            val link = headers[HEADER_LINK_NAME]

            val nextKey: Int? = if (link.isNullOrBlank().not() && link!!.contains("rel=\"next\"")) {
                val matchResult = NEXT_PATTERN.find(link)
                val resultValue = matchResult?.value
                Log.d(TAG, "UserDetailsListPagingSource -> load() -> resultValue = $resultValue")
                if (resultValue == null) null
                else {
                    val nextLinkUri = Uri.parse(resultValue)
                    nextLinkUri.getQueryParameter(QUERY_PARAM_SINCE)?.toIntOrNull()
                }
            } else null
            Log.d(TAG, "UserDetailsListPagingSource -> load() -> link header = $link, next key = $nextKey")

            val body = response.body()?.map {
                userDetailsMapper.map(it)
            } ?: listOf()

            LoadResult.Page(
                data = body,
                prevKey = params.key,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: RuntimeException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
        return loadResult
    }

    override fun getRefreshKey(state: PagingState<Int, UserDetailsModel>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val TAG = "UserListPagingSource"
    }
}