export function setupMultiSelect(selectId, listId, initialValues = []) {
    const select = document.getElementById(selectId);
    const list = document.getElementById(listId);
    const selected = new Map();

    function add(option) {
        const value = option.value;
        if (selected.has(value)) return;

        const item = document.createElement('li');
        item.className = 'selected-item';

        const text = document.createElement('span');
        text.textContent = option.textContent;

        const remove = document.createElement('button');
        remove.type = 'button';
        remove.className = 'selected-item-remove';
        remove.textContent = '×';
        remove.setAttribute('aria-label', 'Remover ' + option.textContent);
        remove.addEventListener('click', () => removeValue(value));

        item.append(text, remove);
        list.appendChild(item);

        option.disabled = true;
        selected.set(value, item);
    }

    function removeValue(value) {
        const item = selected.get(value);
        if (!item) return;

        item.remove();
        selected.delete(value);

        const option = [...select.options].find(o => o.value === value);
        if (option) option.disabled = false;
    }

    select.addEventListener('change', () => {
        const option = select.selectedOptions[0];
        if (!option || !option.value) return;

        add(option);
        select.value = ''; // back to the placeholder
    });

    initialValues.forEach(v => {
        const option = [...select.options].find(o => o.value === String(v));
        if (option) add(option);
    });

    return {
        getValues: () => [...selected.keys()],
        clear: () => [...selected.keys()].forEach(removeValue)
    };
}
