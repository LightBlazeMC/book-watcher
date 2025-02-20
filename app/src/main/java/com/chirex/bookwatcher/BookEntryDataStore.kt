import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chirex.bookwatcher.BookEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "book_entries")

object BookEntryDataStore {
    private val TITLE_KEY = stringPreferencesKey("title")
    private val AUTHOR_KEY = stringPreferencesKey("author")
    private val GENRE_KEY = stringPreferencesKey("genre")
    private val ADDED_KEY = stringPreferencesKey("added")
    private val PROGRESS_KEY = stringPreferencesKey("progress")
    private val RATING_KEY = stringPreferencesKey("rating")

    suspend fun saveEntry(context: Context, entry: BookEntry) {
        context.dataStore.edit { preferences ->
            preferences[TITLE_KEY] = entry.title
            preferences[AUTHOR_KEY] = entry.author
            preferences[GENRE_KEY] = entry.genre
            preferences[ADDED_KEY] = entry.added
            preferences[PROGRESS_KEY] = entry.progress
            preferences[RATING_KEY] = entry.rating
        }
    }

    fun getEntries(context: Context): Flow<List<BookEntry>> {
        return context.dataStore.data.map { preferences ->
            listOf(
                BookEntry(
                    title = preferences[TITLE_KEY] ?: "",
                    author = preferences[AUTHOR_KEY] ?: "",
                    genre = preferences[GENRE_KEY] ?: "",
                    added = preferences[ADDED_KEY] ?: "",
                    progress = preferences[PROGRESS_KEY] ?: "",
                    rating = preferences[RATING_KEY] ?: ""
                )
            )
        }
    }

    suspend fun deleteEntry(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}