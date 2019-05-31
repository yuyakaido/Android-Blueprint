package com.yuyakaido.android.gaia.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.yuyakaido.android.gaia.R
import com.yuyakaido.android.gaia.core.android.GatewayIntentResolverType
import com.yuyakaido.android.gaia.core.java.*
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SelectEnvironmentActivity : DaggerAppCompatActivity(), SelectEnvironmentDialog.OnDismissListener, SessionFooter.AddSessionListener {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SelectEnvironmentActivity::class.java)
        }
    }

    private val disposables = CompositeDisposable()

    @Inject
    lateinit var appStore: AppStore

    @Inject
    lateinit var resolver: GatewayIntentResolverType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_environment)
        setupRecyclerView()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    override fun onDismiss(environment: Environment) {
        val session = Session.newSession(environment)
        AppDispatcher.dispatch(AppSignal.OpenSession(session))
        startGatewayActivity()
    }

    override fun onAddSession() {
        showDialog()
    }

    private fun setupRecyclerView() {
        val adapter = GroupAdapter<ViewHolder>()
        adapter.setOnItemClickListener { item, _ ->
            if (item is SessionItem) {
                val session = item.session
                AppDispatcher.dispatch(AppAction.SelectSession(session))
                startGatewayActivity()
            }
        }
        adapter.setOnItemLongClickListener { item, _ ->
            appStore.single()
                .subscribeBy { state ->
                    if (item is SessionItem && state.sessions.size > 1) {
                        val session = item.session
                        AppDispatcher.dispatch(AppSignal.CloseSession(session))
                    }
                }
                .addTo(disposables)
            return@setOnItemLongClickListener true
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        appStore.observable()
            .subscribeBy { state ->
                adapter.clear()
                adapter.addAll(state.sessions.map { SessionItem(it) })
                adapter.add(SessionFooter(this))
            }
            .addTo(disposables)
    }

    private fun showDialog() {
        val dialog = SelectEnvironmentDialog.newInstance()
        dialog.show(supportFragmentManager, SelectEnvironmentDialog::class.java.simpleName)
    }

    private fun startGatewayActivity() {
        startActivity(resolver.getGatewayActivityIntent(this))
        finish()
    }

}