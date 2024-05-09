import os
import sys
import re

def browse(block_name, member_name):

    print('Operating on block ' + block_name + ' and member ' + member_name)

    folders = ['C:/source/vdyp/VDYP_Master/Source']
    source_files = []
    while len(folders) > 0:
        current_directory = folders.pop()
        entities = os.scandir(current_directory)
        for e in entities:
            if e.is_dir():
                folders.append(e)
            elif e.is_file():
                if e.name.endswith('.for'):
                    source_files.append(e)

    print('Saw ' + str(len(source_files)) + ' source files')

    common_re = re.compile(r'^ *+COMMON.*/' + block_name + r'/', re.IGNORECASE)
    assignment_re = re.compile(r'[^A-Z0-9_]' + member_name + r'([^A-Z0-9_].*=|=)', re.IGNORECASE)
    usage_re = re.compile(r'[^A-Z0-9_]' + member_name + r'[^A-Z0-9_]', re.IGNORECASE)

    for s in source_files:
        sf = open(s)
        block_declarations = []
        usages = []
        assignments = []
        for line in sf.readlines():
            if line[0].upper() == 'C':
                continue
            if common_re.search(line):
                block_declarations.append(line)
            elif line.strip().upper().startswith('DIMENSION'):
                pass
            elif assignment_re.search(line):
                assignments.append(line)
            elif line.strip().upper().startswith('&'):
                pass
            elif usage_re.search(line):
                usages.append(line)

        if len(block_declarations) > 0 and (len(assignments) > 0 or len(usages) > 0):
            print(s.name)
            if len(assignments) > 0:
                for l in assignments:
                    print('   A    ' + l.strip())
            if len(usages) > 0:
                for l in usages:
                    print('   U    ' + l.strip())

        sf.close()

if __name__ == '__main__':

    block_name = sys.argv[1]
    member_name = sys.argv[2]

    browse(block_name, member_name)