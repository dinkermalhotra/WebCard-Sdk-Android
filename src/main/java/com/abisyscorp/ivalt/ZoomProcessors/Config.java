//
// FaceTec Device SDK config file.
// Auto-generated via the FaceTec SDK Configuration Wizard
//
package com.abisyscorp.ivalt.ZoomProcessors;

import android.content.Context;

import com.facetec.sdk.FaceTecCustomization;
import com.facetec.sdk.FaceTecOverlayCustomization;
import com.facetec.sdk.FaceTecSDK;

public class Config {
    // -------------------------------------
    // REQUIRED
    // Available at https://dev.facetec.com/#/account
    // NOTE: This field is auto-populated by the FaceTec SDK Configuration Wizard.
    public static String DeviceKeyIdentifier = "dz2ysZrt1AfnaoBpyZT4HR8Jmum7f1zc";

    // -------------------------------------
    // REQUIRED
    // The URL to call to process FaceTec SDK Sessions.
    // In Production, you likely will handle network requests elsewhere and without the use of this variable.
    // See https://dev.facetec.com/#/security-best-practices?link=facetec-server-rest-endpoint-security for more information.
    // NOTE: This field is auto-populated by the FaceTec SDK Configuration Wizard.
    public static String BaseURL = "https://api.facetec.com/api/v3.1/biometrics";

    // -------------------------------------
    // REQUIRED
    // The FaceScan Encryption Key you define for your application.
    // Please see https://dev.facetec.com/#/licensing-and-encryption-keys for more information.
    // NOTE: This field is auto-populated by the FaceTec SDK Configuration Wizard.
    static String PublicFaceScanEncryptionKey =
        "-----BEGIN PUBLIC KEY-----\n" +
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5PxZ3DLj+zP6T6HFgzzk\n" +
        "M77LdzP3fojBoLasw7EfzvLMnJNUlyRb5m8e5QyyJxI+wRjsALHvFgLzGwxM8ehz\n" +
        "DqqBZed+f4w33GgQXFZOS4AOvyPbALgCYoLehigLAbbCNTkeY5RDcmmSI/sbp+s6\n" +
        "mAiAKKvCdIqe17bltZ/rfEoL3gPKEfLXeN549LTj3XBp0hvG4loQ6eC1E1tRzSkf\n" +
        "GJD4GIVvR+j12gXAaftj3ahfYxioBH7F7HQxzmWkwDyn3bqU54eaiB7f0ftsPpWM\n" +
        "ceUaqkL2DZUvgN0efEJjnWy5y1/Gkq5GGWCROI9XG/SwXJ30BbVUehTbVcD70+ZF\n" +
        "8QIDAQAB\n" +
        "-----END PUBLIC KEY-----";

    // -------------------------------------
    // Convenience method to initialize the FaceTec SDK.
    // NOTE: This function is auto-populated by the FaceTec SDK Configuration Wizard based on the Keys issued to your Apps.
    public static void initializeFaceTecSDKFromAutogeneratedConfig(Context context, FaceTecSDK.InitializeCallback callback) {
        FaceTecCustomization zoomCustomization = new FaceTecCustomization();
        FaceTecOverlayCustomization overlayCustomization = new FaceTecOverlayCustomization();
        overlayCustomization.showBrandingImage = false;
        overlayCustomization.backgroundColor = android.R.color.transparent;
        zoomCustomization.setOverlayCustomization(overlayCustomization);
        FaceTecSDK.setCustomization(zoomCustomization);
        FaceTecSDK.initializeInDevelopmentMode(context, DeviceKeyIdentifier, PublicFaceScanEncryptionKey, callback);
    }

    // -------------------------------------
    // This app can modify the customization to demonstrate different look/feel preferences
    // NOTE: This function is auto-populated by the FaceTec SDK Configuration Wizard based on your UI Customizations you picked in the Configuration Wizard GUI.
    public static FaceTecCustomization retrieveConfigurationWizardCustomization() {
        return new FaceTecCustomization();
    }

    public static FaceTecCustomization currentCustomization = retrieveConfigurationWizardCustomization();

    // -------------------------------------
    // Boolean to indicate the FaceTec SDK Configuration Wizard was used to generate this file.
    // In this Sample App, if this variable is true, a "Config Wizard Theme" will be added to this App's Design Showcase,
    // and choosing this option will set the FaceTec SDK UI/UX Customizations to the Customizations that you selected in the
    // Configuration Wizard.
    public static boolean wasSDKConfiguredWithConfigWizard = false;
}
