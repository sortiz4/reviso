#!/usr/bin/env python
import argparse
import os
from io import BytesIO
from subprocess import run
from urllib import request
from zipfile import ZipFile

BASE_PATH = os.path.dirname(os.path.abspath(__file__))
ICONS_SRC_URL = 'https://github.com/sortiz4/reviso/releases/download/1.0/1.0.zip'
ICONS_SRC_SIZES = [16, 32, 64, 128, 256]
ICONS_DEST_PATH = os.path.join(BASE_PATH, 'src/main/resources/reviso/icon')
WRAPPER_SRC_PATH = os.path.join(BASE_PATH, 'src/main/rust/reviso.rs')
WRAPPER_DEST_PATH = os.path.join(BASE_PATH, 'src/dist/bin')
WRAPPER_SETUP_TASK = ['rustc', WRAPPER_SRC_PATH, '--out-dir', WRAPPER_DEST_PATH, '-C', 'opt-level=3']


class Command:

    def __init__(self) -> None:
        # Construct the argument parser
        options = {
            'description': 'Prepares the workspace.',
        }
        parser = argparse.ArgumentParser(**options)

        # Add the arguments to the parser
        arguments = [
            [
                [
                    '-i',
                    '--icons',
                ],
                {
                    'action': 'store_true',
                    'help': 'Downloads the icons.',
                },
            ],
            [
                [
                    '-w',
                    '--wrapper',
                ],
                {
                    'action': 'store_true',
                    'help': 'Compiles the wrapper.',
                },
            ],
        ]
        for argument in arguments:
            parser.add_argument(*argument[0], **argument[1])

        # Parse the arguments from the system
        self.args = parser.parse_args()

    def handle(self) -> None:
        def icons() -> None:
            def get_icon_name(icon_size: int) -> str:
                return '{}.png'.format(icon_size)

            # Stop if one of the icons exist
            if os.path.exists(os.path.join(ICONS_DEST_PATH, get_icon_name(ICONS_SRC_SIZES[0]))):
                return

            # Make the directories if they don't exist
            if not os.path.exists(ICONS_DEST_PATH):
                os.makedirs(ICONS_DEST_PATH)

            # Download the icon archive
            archive = ZipFile(BytesIO(request.urlopen(ICONS_SRC_URL).read()))

            # Extract the icons
            for size in ICONS_SRC_SIZES:
                name = get_icon_name(size)
                content = archive.read('icons/png/{}'.format(name))
                with open(os.path.join(ICONS_DEST_PATH, name), 'wb') as icon:
                    icon.write(content)

        def wrapper() -> None:
            # Compile the wrapper
            run(WRAPPER_SETUP_TASK)

        if self.args.icons:
            icons()
        if self.args.wrapper:
            wrapper()


if __name__ == '__main__':
    Command().handle()
