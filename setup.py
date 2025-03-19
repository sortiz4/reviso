#!/usr/bin/env python
import argparse
import os
from io import BytesIO
from subprocess import run
from urllib import request
from zipfile import ZipFile


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

    def run(self) -> None:
        base_path = os.path.dirname(os.path.abspath(__file__))

        def icons() -> None:
            icon_url = 'https://github.com/sortiz4/reviso/releases/download/1.0/1.0.zip'
            icon_path = os.path.join(base_path, 'src/main/resources/reviso/icon')
            icon_sizes = [16, 32, 64, 128, 256]

            def get_icon_name(icon_size: int) -> str:
                return '{}.png'.format(icon_size)

            # Stop if one of the icons exist
            if os.path.exists(os.path.join(icon_path, get_icon_name(icon_sizes[0]))):
                return

            # Make the directories if they don't exist
            if not os.path.exists(icon_path):
                os.makedirs(icon_path)

            # Download the icon archive
            archive = ZipFile(BytesIO(request.urlopen(icon_url).read()))

            # Extract the icons
            for size in icon_sizes:
                name = get_icon_name(size)
                content = archive.read('icons/png/{}'.format(name))

                with open(os.path.join(icon_path, name), 'wb') as icon:
                    icon.write(content)

        def wrapper() -> None:
            arguments = [
                'rustc',
                os.path.join(base_path, 'src/main/rust/reviso.rs'),
                '--out-dir',
                os.path.join(base_path, 'src/dist/bin'),
                '-C',
                'opt-level=3',
            ]

            # Compile the wrapper
            run(arguments)

        if self.args.icons:
            icons()

        if self.args.wrapper:
            wrapper()


if __name__ == '__main__':
    Command().run()
