#!/usr/bin/env python
import argparse
import os
from io import BytesIO
from subprocess import run
from urllib import request
from zipfile import ZipFile

COMMAND_HELP = 'Prepares the workspace.'
COMMAND_DIR = os.path.dirname(os.path.abspath(__file__))
BIN_SRC = os.path.join(COMMAND_DIR, 'src/main/rust/reviso.rs')
BIN_DEST = os.path.join(COMMAND_DIR, 'src/dist/bin')
BIN_TASK = ['rustc', BIN_SRC, '--out-dir', BIN_DEST, '-C', 'opt-level=3']
BIN_HELP = 'Compiles the command...'
ICONS_HELP = 'Downloads the icons.'
ICONS_SIZES = [16, 32, 64, 128, 256]
ICONS_DEST = os.path.join(COMMAND_DIR, 'src/main/resources/reviso/icon')
ICONS_URL = 'https://github.com/sortiz4/reviso/releases/download/1.0/1.0.zip'


class Command:
    help = COMMAND_HELP

    def __init__(self):
        parser = argparse.ArgumentParser(description=self.help)
        parser.add_argument('-i', '--icons', action='store_true', help=ICONS_HELP)
        parser.add_argument('-b', '--bin', action='store_true', help=BIN_HELP)
        self.args = parser.parse_args()

    def handle(self):
        if self.args.icons:
            self.icons()
        if self.args.bin:
            self.bin()

    @classmethod
    def bin(cls):
        # Compile the start???
        run(BIN_TASK)

    @classmethod
    def icons(cls):
        def get_icon_name(icon_size):
            return '{}.png'.format(icon_size)

        # Check if any of the icons exist
        if os.path.exists(os.path.join(ICONS_DEST, get_icon_name(ICONS_SIZES[0]))):
            return

        # Make the directory if it doesn't exist
        if not os.path.exists(ICONS_DEST):
            os.makedirs(ICONS_DEST)

        # Download the icon archive to memory
        archive = ZipFile(BytesIO(request.urlopen(ICONS_URL).read()))

        # Extract the icons
        for size in ICONS_SIZES:
            name = get_icon_name(size)
            content = archive.read('icons/png/{}'.format(name))
            with open(os.path.join(ICONS_DEST, name), 'wb') as icon:
                icon.write(content)


if __name__ == '__main__':
    Command().handle()
