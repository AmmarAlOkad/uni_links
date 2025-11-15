package name.avioli.unilinks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * UniLinksPlugin - Flutter Android embedding v2 only implementation.
 *
 * Note:
 * - This version deliberately does NOT reference PluginRegistry.Registrar (v1 API)
 *   so it compiles with newer Flutter/Gradle toolchains.
 * - If you need to preserve a v1 registerWith signature for older apps,
 *   consider upgrading your project to the v2 embedding or forking the plugin.
 */
public class UniLinksPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {
  private MethodChannel channel;
  private Activity activity;

  // Called when plugin is attached to engine (v2)
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    setupChannel(binding.getBinaryMessenger());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    tearDownChannel();
  }

  // ActivityAware implementations to keep a reference to the Activity for intents
  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
    // Optional: handle the intent that started the activity
    // You can forward the initial intent URI to Dart on attach if needed
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    this.activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {
    this.activity = null;
  }

  private void setupChannel(BinaryMessenger messenger) {
    channel = new MethodChannel(messenger, "uni_links/messages");
    channel.setMethodCallHandler(this);
  }

  private void tearDownChannel() {
    if (channel != null) {
      channel.setMethodCallHandler(null);
      channel = null;
    }
  }

  // Minimal method call handler — implement the original plugin methods here as needed.
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "getInitialUri":
        result.success(getInitialUriString());
        break;
      default:
        result.notImplemented();
    }
  }

  private String getInitialUriString() {
    if (activity == null) return null;
    Intent intent = activity.getIntent();
    if (intent == null) return null;
    Uri data = intent.getData();
    return (data != null) ? data.toString() : null;
  }
}
