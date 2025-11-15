package name.avioli.unilinks;

import android.app.Activity;
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * Minimal v2-compatible plugin implementation that also provides a v1-style registerWith
 * for projects still using the old embedding.
 *
 * NOTE: This is a minimal skeleton mostly to fix the compile error. If the plugin has
 * additional functionality (intent handling, streams, etc.) you should port that logic here
 * as needed.
 */
public class UniLinksPlugin implements FlutterPlugin, ActivityAware {
  private MethodChannel channel;
  private Activity activity;

  // v1 embedding registration (keeps compatibility)
  @SuppressWarnings("deprecation")
  public static void registerWith(Registrar registrar) {
    UniLinksPlugin plugin = new UniLinksPlugin();
    plugin.activity = registrar.activity();
    plugin.setupChannel(registrar.messenger());
  }

  // v2 embedding
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    setupChannel(binding.getBinaryMessenger());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    tearDownChannel();
  }

  // ActivityAware callbacks to keep track of Activity if needed by plugin logic
  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
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

  // Channel setup / teardown
  private void setupChannel(BinaryMessenger messenger) {
    channel = new MethodChannel(messenger, "uni_links/messages");
    // setMethodCallHandler(...) -> plug in your method call handler if needed
  }

  private void tearDownChannel() {
    if (channel != null) {
      channel.setMethodCallHandler(null);
      channel = null;
    }
  }
}