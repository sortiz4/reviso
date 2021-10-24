use std::env;
use std::fs;
use std::process::Command;
use std::process::Stdio;

fn main() {
    // Get the module path
    let mut module_path = match env::current_exe() {
        Ok(p) => p.to_path_buf(),
        _ => return,
    };
    module_path.pop();
    module_path.pop();
    module_path.push("lib");

    // Construct the class path
    let class_path = {
        fs::read_dir(&module_path)
            .unwrap()
            .map(|entry| entry.unwrap().path().to_str().unwrap().to_owned())
            .collect::<Vec<_>>()
            .join(if cfg![target_os = "windows"] { ";" } else { ":" })
    };

    // Construct the arguments
    let default_arguments = &[
        "--module-path", module_path.to_str().unwrap(),
        "--add-modules", "javafx.controls,javafx.fxml",
        "-classpath", &class_path,
        "reviso.Main",
        "--",
    ];
    let final_arguments = {
        default_arguments
            .iter()
            .map(|string| string.to_string())
            .chain(env::args().skip(1).map(|string| string.to_string()))
    };

    // Spawn the Java process
    Command::new(if cfg![target_os = "windows"] { "javaw" } else { "java" })
        .stdin(Stdio::inherit())
        .stdout(Stdio::inherit())
        .stderr(Stdio::inherit())
        .args(final_arguments)
        .spawn()
        .unwrap();
}
