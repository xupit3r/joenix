use std::string::{String, FromUtf8Error};
use std::process::Command;

const MEDIA_PLAYER: &str = "/org/mpris/MediaPlayer2";

//struct Device {
//    id: String,
//    is_active: bool,
//    is_private_session: bool,
//    is_restricted: false,
//    name: String,
//    type: String,
//    volume_percent: i16
//}

/// returns spotifyd process ID
fn sp_pid() -> String {
    String::from_utf8(
        Command::new("pgrep")
            .arg("spotifyd")
            .output()
            .expect("Failed to execute command")
            .stdout
    ).expect("get_pid -- invalid UTF-8")
}

/// returns spotifyd instance URI
fn sp_instance(pid: &str) -> String {
    format!("org.mpris.MediaPlayer2.spotifyd.instance{pid}")
}

/// returns a player method URI
fn sp_player(method: &str) -> String {
    format!("org.mpris.MediaPlayer2.Player.{method}")
}

/// returns a URI for the thing we want to do 
fn sp_thing(uri: &str) -> String {
    format!("string:spotify:{uri}")
}

fn dbus_message(instance: &str,
                method_uri: &str,
                thing: &str) -> Result<String, FromUtf8Error> {
    String::from_utf8(
        Command::new("dbus-send")
            .arg("--print-reply")
            .arg(format!("--dest={instance}"))
            .arg(MEDIA_PLAYER)
            .arg(method_uri)
            .arg(thing.clone())
            .output()
            .expect(format!("dbus-send failed for {thing}").as_str())
            .stdout
    )
}

//fn sp_devices() -> Vec<Device> {}
//fn sp_start(deviceId: &str) -> &str {}

fn main() {
    let pid: String = sp_pid();
    let instance: String = sp_instance(pid.trim());
    let method_uri: String = sp_player("OpenUri");
    let thing: String = sp_thing("playlist:37i9dQZF1EpfWzY4TEI80Y");
    

    match dbus_message(instance.trim(), method_uri.trim(), thing.trim()) {
        Ok(str) => println!("{str:?}"),
        Err(e) => println!("{e:?}")
    };
}
