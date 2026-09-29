package app.aaps.activities

import android.content.Intent
import android.os.Bundle
import app.aaps.MainActivity
import app.aaps.plugins.configuration.activities.DaggerAppCompatActivityWithResult

class WizardLaunchActivity : DaggerAppCompatActivityWithResult() {

    companion object {
        const val EXTRA_CARBS = "carbs"
        const val EXTRA_NOTES = "notes"
        const val EXTRA_SOURCE = "source"

        // Whitelist: only CarbCam may open the wizard externally
        private val ALLOWED_CALLERS = setOf(
            "de.be10.carbcam",
            "de.be10.carbcam.debug",
            "de.be10.carbcam-debug",
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val caller = callingPackage ?: referrer?.host
        if (caller !in ALLOWED_CALLERS) {
            finish()
            return
        }

        val carbs = intent.getIntExtra(EXTRA_CARBS, 0)
        val notes = intent.getStringExtra(EXTRA_NOTES) ?: ""

        if (carbs <= 0 || carbs > 80) {
            finish()
            return
        }

        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("open_wizard_carbs", carbs)
            putExtra("open_wizard_notes", notes)
        })
        finish()
    }
}
