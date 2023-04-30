use std::string::String;
use std::process::Command;

/// returns spotifyd process ID
fn get_pid() -> String {
    String::from_utf8(
        Command::new("pgrep")
            .arg("spotifyd")
            .output()
            .expect("Failed to execute command")
            .stdout
    ).expect("get_pid -- invalid UTF-8")
}

fn main() {
    println!("spotifyd PID {}", get_pid())
}
