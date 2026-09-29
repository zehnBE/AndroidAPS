package app.aaps.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * External entry point for 10BE CarbCam (de.be10.carbcam) to open the Bolus
 * Wizard prefilled with a carb value. Whitelisted at callingPackage level;
 * everyone else is silently dropped.
 *
 * Forwards to ComposeMainActivity with "external_carbs" / "external_notes"
 * extras, which then navigates to AppRoute.WizardDialog.createRoute(carbs, notes).
 * The user still has to confirm in the wizard - no silent bolus.
 */
class WizardLaunchActivity : Activity() {

    companion object {
        const val EXTRA_CARBS = "carbs"
        const val EXTRA_NOTES = "notes"

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

        startActivity(Intent(this, app.aaps.ComposeMainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("external_carbs", carbs)
            putExtra("external_notes", notes)
        })
        finish()
    }
}
