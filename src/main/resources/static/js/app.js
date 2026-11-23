document.addEventListener('DOMContentLoaded', () => {
  const formatMoney = amount => '$ ' + new Intl.NumberFormat('es-CO', {
    maximumFractionDigits: 0
  }).format(Math.round(amount * 1000));

  const updateVentaTotal = () => {
    const form = document.querySelector('[data-venta-form]');
    if (!form) return;

    let total = 0;
    form.querySelectorAll('#productos .item-row').forEach(row => {
      const product = row.querySelector('[data-product-select]');
      const price = Number(row.querySelector('[data-unit-price]').value) || 0;
      const quantity = Number(row.querySelector('[data-quantity]').value) || 0;
      const subtotal = product.value ? price * quantity : 0;
      total += subtotal;
      row.querySelector('[data-line-total]').textContent = formatMoney(subtotal);
    });

    form.querySelectorAll('#servicios .item-row').forEach(row => {
      const price = Number(row.querySelector('[data-service-price]').value) || 0;
      total += price;
      row.querySelector('[data-line-total]').textContent = formatMoney(price);
    });

    form.querySelector('#ventaTotal').textContent = formatMoney(total);
  };

  document.querySelectorAll('[data-confirm]').forEach(button => {
    button.addEventListener('click', event => {
      if (!window.confirm(button.dataset.confirm)) {
        event.preventDefault();
        event.stopPropagation();
      }
    });
  });

  document.querySelectorAll('[data-clone-row]').forEach(button => {
    button.addEventListener('click', () => {
      const body = document.getElementById(button.dataset.cloneRow);
      const first = body?.querySelector('.item-row');
      if (!first) return;

      const clone = first.cloneNode(true);
      clone.querySelectorAll('input').forEach(input => {
        input.value = input.name === 'cantidad' ? 1 : '';
      });
      clone.querySelectorAll('select').forEach(select => {
        select.selectedIndex = 0;
      });
      clone.querySelectorAll('[data-line-total]').forEach(line => {
        line.textContent = '$ 0';
      });
      body.appendChild(clone);
      updateVentaTotal();
    });
  });

  document.addEventListener('click', event => {
    const remove = event.target.closest('[data-remove-row]');
    if (!remove) return;

    const body = remove.closest('tbody');
    const rows = body?.querySelectorAll('.item-row') || [];
    if (rows.length > 1) {
      remove.closest('.item-row').remove();
      updateVentaTotal();
    }
  });

  const ventaForm = document.querySelector('[data-venta-form]');
  if (ventaForm) {
    ventaForm.addEventListener('change', event => {
      if (event.target.matches('[data-product-select]')) {
        const price = event.target.selectedOptions[0]?.dataset.price || '';
        event.target.closest('.item-row').querySelector('[data-unit-price]').value = price;
      }
      updateVentaTotal();
    });
    ventaForm.addEventListener('input', updateVentaTotal);
    updateVentaTotal();
  }

  const ventaSelect = document.getElementById('ventaId');
  const productoSelect = document.getElementById('productoId');
  if (ventaSelect && productoSelect) {
    ventaSelect.addEventListener('change', async () => {
      productoSelect.innerHTML = '<option>Cargando...</option>';
      if (!ventaSelect.value) return;
      const response = await fetch('/devoluciones/venta/' + ventaSelect.value + '/productos');
      const items = await response.json();
      productoSelect.innerHTML = items.map(item =>
        '<option value="' + item.productoId + '">' + item.productoNombre
        + ' (vendidos: ' + item.cantidad + ')</option>').join('');
    });
  }
});
