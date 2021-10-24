#!/usr/bin/env python
import argparse
import os
from io import BytesIO
from subprocess import run
from urllib import request
from zipfile import ZipFile

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ICONS_URL = 'https://github.com/sortiz4/reviso/releases/download/1.0/1.0.zip'
ICONS_DEST = os.path.join(BASE_DIR, 'src/main/resources/reviso/icon')
ICONS_SIZES = [16, 32, 64, 128, 256]
WRAPPER_SRC = os.path.join(BASE_DIR, 'src/main/rust/reviso.rs')
WRAPPER_DEST = os.path.join(BASE_DIR, 'src/dist/bin')
WRAPPER_SETUP = ['rustc', WRAPPER_SRC, '--out-dir', WRAPPER_DEST, '-C', 'opt-level=3']


class Command:

    def __init__(self):
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

    def handle(self):
        def icons():
            def get_icon_name(icon_size):
                return '{}.png'.format(icon_size)

            # Stop if one of the icons exist
            if os.path.exists(os.path.join(ICONS_DEST, get_icon_name(ICONS_SIZES[0]))):
                return

            # Make the directories if they don't exist
            if not os.path.exists(ICONS_DEST):
                os.makedirs(ICONS_DEST)

            # Download the icon archive
            archive = ZipFile(BytesIO(request.urlopen(ICONS_URL).read()))

            # Extract the icons
            for size in ICONS_SIZES:
                name = get_icon_name(size)
                content = archive.read('icons/png/{}'.format(name))
                with open(os.path.join(ICONS_DEST, name), 'wb') as icon:
                    icon.write(content)

        def wrapper():
            # Compile the wrapper
            run(WRAPPER_SETUP)

        if self.args.icons:
            icons()
        if self.args.wrapper:
            wrapper()


if __name__ == '__main__':
    Command().handle()
