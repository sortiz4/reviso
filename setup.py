#!/usr/bin/env python
import argparse
import os
from io import BytesIO
from urllib import request
from zipfile import ZipFile

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ICON_HELP = 'Downloads the icon assets.'
ICON_SIZES = [16, 32, 64, 128, 256]
ICON_DIR = os.path.join(BASE_DIR, 'src/main/resources/reviso/icon')
ICON_URL = 'https://github.com/sortiz4/reviso/releases/download/1.0/1.0.zip'


class Command:
    help = 'Downloads the required assets.'

    def __init__(self):
        parser = argparse.ArgumentParser(description=self.help)
        parser.add_argument('-i', '--icons', action='store_true', help=ICON_HELP)
        self.args = parser.parse_args()

    def handle(self):
        if self.args.icons:
            self.icons()

    @classmethod
    def icons(cls):
        # Make the directory if it doesn't exist
        if not os.path.exists(ICON_DIR):
            os.makedirs(ICON_DIR)

        # Download the icon archive to memory
        buffer = request.urlopen(ICON_URL).read()
        archive = BytesIO(buffer)

        # Extract the icons
        archive = ZipFile(archive)
        for size in ICON_SIZES:
            name = '{}.png'.format(size)
            buffer = archive.read('icons/png/' + name)
            path = os.path.join(ICON_DIR, name)
            with open(path, 'wb') as icon:
                icon.write(buffer)


if __name__ == '__main__':
    Command().handle()
