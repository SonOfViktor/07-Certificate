import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as certificateAction from '../../../../../store/certificates/certificateSlice';
import CertificateForm from '../../../../../components/admin/certificate-modal/CertificateForm';
import {createCertificate} from '../../../../helpers/certificateHelper';

jest.mock('react-redux');

describe('coupon list component', () => {
  const categoryList = ['Auto', 'Tech', 'Sport'];
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');

  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSelector.mockReturnValue(categoryList);
  });

  test('add certificate form render', async () => {
    render(<CertificateForm />);

    expect(screen.getByRole('textbox', {name: 'Title'})).toHaveValue('');
    expect(
      screen.getByRole('spinbutton', {name: 'Duration'})
    ).toBeInTheDocument();
    expect(screen.getByRole('textbox', {name: 'Description'})).toHaveValue('');
    expect(screen.getByRole('spinbutton', {name: 'Price'})).toBeInTheDocument();
    expect(screen.getByRole('textbox', {name: 'Image'})).toBeInTheDocument();
    expect(screen.getByRole('textbox', {name: 'Tags'})).toHaveValue('');
  });

  test('edit certificate form render', async () => {
    const certificate = createCertificate(
      1,
      'Certificate',
      'some description',
      10,
      55,
      [{name: 'tag1'}, {name: 'tag2'}, {name: 'tag3'}]
    );
    certificate.category = 'Sport';

    render(<CertificateForm certificate={certificate} />);

    expect(screen.getByRole('textbox', {name: 'Title'})).toHaveValue(
      'Certificate'
    );
    expect(screen.getByRole('spinbutton', {name: 'Duration'})).toHaveValue(10);
    expect(screen.getByDisplayValue(/sport/i)).toBeInTheDocument();
    expect(screen.getByRole('textbox', {name: 'Description'})).toHaveValue(
      'some description'
    );
    expect(screen.getByRole('spinbutton', {name: 'Price'})).toHaveValue(55);
    expect(screen.getAllByText(/tag/)).toHaveLength(3);
  });

  test('submit form with empty fields', async () => {
    render(<CertificateForm />);

    const confirmButton = screen.getByRole('button', {name: 'Save'});

    fireEvent.submit(confirmButton);

    expect(await screen.findAllByText(/not be empty/)).toHaveLength(6);
    expect(dispatch).not.toHaveBeenCalled();
  });

  test('title error message', async () => {
    const errorMessage = /title .* 6 - 30 latin or cyrillic .* digits/i;

    render(<CertificateForm />);

    const confirmButton = screen.getByRole('button', {name: 'Save'});
    const titleInput = screen.getByRole('textbox', {name: 'Title'});

    userEvent.type(titleInput, 'asdfg');
    fireEvent.submit(confirmButton);
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(titleInput, 'asdfg');
    await waitFor(() => expect(screen.queryByText(errorMessage)).toBeNull());

    userEvent.type(titleInput, 'asdfgasdfgasdfgasdfga');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.clear(titleInput);
    userEvent.type(titleInput, '  asdfgqwert');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  test('duration error message', async () => {
    const minErrorMessage = 'Duration must not be less than 1';
    const maxErrorMessage = 'Duration must be less than 100';

    render(<CertificateForm />);

    const confirmButton = screen.getByRole('button', {name: 'Save'});
    const durationInput = screen.getByRole('spinbutton', {name: 'Duration'});

    userEvent.type(durationInput, '0');
    fireEvent.submit(confirmButton);
    expect(await screen.findByText(minErrorMessage)).toBeInTheDocument();

    userEvent.clear(durationInput);
    userEvent.type(durationInput, '1');
    await waitFor(() => expect(screen.queryByText(minErrorMessage)).toBeNull());

    userEvent.clear(durationInput);
    userEvent.type(durationInput, '101');
    expect(await screen.findByText(maxErrorMessage)).toBeInTheDocument();

    userEvent.clear(durationInput);
    userEvent.type(durationInput, '99');
    await waitFor(() => expect(screen.queryByText(maxErrorMessage)).toBeNull());
  });

  test('description error message', async () => {
    const errorMessage = /description .* 12 - 1000 letters and digits/i;

    render(<CertificateForm />);

    const confirmButton = screen.getByRole('button', {name: 'Save'});
    const descriptionInput = screen.getByRole('textbox', {name: 'Description'});

    userEvent.type(descriptionInput, 'asdfg');
    fireEvent.submit(confirmButton);
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(descriptionInput, 'asdfgas');
    await waitFor(() => expect(screen.queryByText(errorMessage)).toBeNull());

    userEvent.type(descriptionInput, new Array(988).fill('a').join(''));
    await waitFor(() => expect(screen.queryByText(errorMessage)).toBeNull());

    userEvent.type(descriptionInput, 'a');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.clear(descriptionInput);
    userEvent.type(descriptionInput, ' asdfgqwertjklddz');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  test('price error message', async () => {
    const minErrorMessage = 'Price must be more than 0';
    const maxErrorMessage = 'Price must be less than 1000';

    render(<CertificateForm />);

    const confirmButton = screen.getByRole('button', {name: 'Save'});
    const priceInput = screen.getByRole('spinbutton', {name: 'Price'});

    userEvent.type(priceInput, '0');
    fireEvent.submit(confirmButton);
    expect(await screen.findByText(minErrorMessage)).toBeInTheDocument();

    userEvent.clear(priceInput);
    userEvent.type(priceInput, '0.01');
    await waitFor(() => expect(screen.queryByText(minErrorMessage)).toBeNull());

    userEvent.clear(priceInput);
    userEvent.type(priceInput, '1000');
    expect(await screen.findByText(maxErrorMessage)).toBeInTheDocument();

    userEvent.clear(priceInput);
    userEvent.type(priceInput, '999.99');
    await waitFor(() => expect(screen.queryByText(maxErrorMessage)).toBeNull());
  });

  test('image error message', async () => {
    const errorMessage =
      'File must be an image with jpeg, jpg or png extension';

    const file = new File(['hello'], 'hello.json', {
      type: 'application/json',
    });

    render(<CertificateForm />);

    const confirmButton = screen.getByRole('button', {name: 'Save'});
    const imageInput = screen.getByRole('textbox', {name: 'Image'});
    const data = mockData([file]);
    dispatchEvt(imageInput, 'dragenter', data);

    expect(await screen.findByDisplayValue('hello.json')).toBeInTheDocument();

    fireEvent.submit(confirmButton);

    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  describe('tags input', () => {
    test('tags pattern error message', async () => {
      const patternErrorMessage =
        /tag name .* 3 - 15 latin or cyrillic .* and digits/i;

      render(<CertificateForm />);

      const confirmButton = screen.getByRole('button', {name: 'Save'});
      const addButton = screen.getByRole('button', {name: 'Add'});
      const tagInput = screen.getByRole('textbox', {name: 'Tags'});

      const checkTagPattern = async tagName => {
        userEvent.click(screen.getByTestId('CancelIcon'));
        await waitFor(() =>
          expect(screen.queryByText(patternErrorMessage)).toBeNull()
        );
        userEvent.type(tagInput, tagName);
        userEvent.click(addButton);
        expect(
          await screen.findByText(patternErrorMessage)
        ).toBeInTheDocument();
      };

      userEvent.type(tagInput, 'as');
      userEvent.click(addButton);
      userEvent.click(confirmButton);
      expect(await screen.findByText(patternErrorMessage)).toBeInTheDocument();
      await checkTagPattern('  asv');
      await checkTagPattern('');
    });

    test('tags max quantity error message', async () => {
      const maxTagsErrorMessage = 'Max quantity of tags is 20';

      render(<CertificateForm />);

      const confirmButton = screen.getByRole('button', {name: 'Save'});
      const addButton = screen.getByRole('button', {name: 'Add'});
      const tagInput = screen.getByRole('textbox', {name: 'Tags'});

      for (let i = 1; i <= 20; i++) {
        userEvent.type(tagInput, 'tag' + i);
        userEvent.click(addButton);
        expect(await screen.findByText('tag' + i)).toBeInTheDocument();
      }

      userEvent.click(confirmButton);

      await waitFor(() =>
        expect(screen.queryByText(maxTagsErrorMessage)).toBeNull()
      );

      userEvent.type(tagInput, 'tag21');
      userEvent.click(addButton);
      expect(await screen.findByText(maxTagsErrorMessage)).toBeInTheDocument();
    });

    test('duplicate error message', async () => {
      const duplicateMessage = "The tag list can't contain duplicates";
      render(<CertificateForm />);

      const confirmButton = screen.getByRole('button', {name: 'Save'});
      const addButton = screen.getByRole('button', {name: 'Add'});
      const tagInput = screen.getByRole('textbox', {name: 'Tags'});

      userEvent.type(tagInput, 'tag');
      userEvent.click(addButton);
      userEvent.click(confirmButton);
      expect(await screen.findByText('tag')).toBeInTheDocument();
      expect(screen.queryByText(duplicateMessage)).toBeNull();

      userEvent.type(tagInput, 'tag');
      userEvent.click(addButton);
      expect(await screen.findByText(duplicateMessage)).toBeInTheDocument();
    });
  });

  test('update certificate form submit', async () => {
    const certificate = createCertificate(
      1,
      'Certificate',
      'There is some description!',
      10,
      55,
      [{name: 'tag1'}]
    );
    certificate.category = 'Sport';
    const onClose = jest.fn();
    const updateAction = jest.spyOn(certificateAction, 'updateCertificate');

    render(<CertificateForm certificate={certificate} onClose={onClose} />);

    const confirmButton = screen.getByRole('button', {name: 'Save'});
    const titleInput = screen.getByRole('textbox', {name: 'Title'});
    const descriptionInput = screen.getByRole('textbox', {name: 'Description'});

    userEvent.type(titleInput, '123');
    userEvent.type(descriptionInput, ' Extra description.');
    userEvent.click(confirmButton);

    await waitFor(() => {
      expect(dispatch).toHaveBeenCalled();
    });
    expect(updateAction).toHaveBeenCalled();
    expect(onClose).toHaveBeenCalled();

    const formData = updateAction.mock.calls[0][0];
    expect(formData.get('name')).toBe('Certificate123');
    expect(formData.get('description')).toBe(
      'There is some description! Extra description.'
    );
  });

  test('reset form', async () => {
    const onClose = jest.fn();

    render(<CertificateForm onClose={onClose} />);

    const resetButton = screen.getByRole('button', {name: 'Reset'});
    const titleInput = screen.getByRole('textbox', {name: 'Title'});
    const durationInput = screen.getByRole('spinbutton', {name: 'Duration'});
    const descriptionInput = screen.getByRole('textbox', {name: 'Description'});
    const priceInput = screen.getByRole('spinbutton', {name: 'Price'});

    userEvent.type(titleInput, 'Certificate');
    userEvent.type(durationInput, '10');
    userEvent.type(descriptionInput, 'There is some description!');
    userEvent.type(priceInput, '50.50');

    userEvent.click(resetButton);
    expect(titleInput).toHaveValue('');
    expect(durationInput).toHaveValue(null);
    expect(descriptionInput).toHaveValue('');
    expect(priceInput).toHaveValue(null);
  });
});

function dispatchEvt(node, type, data) {
  const event = new Event(type, {bubbles: true});
  Object.assign(event, data);
  fireEvent.drop(node, event);
}

function mockData(files) {
  return {
    dataTransfer: {
      files,
      items: files.map(file => ({
        kind: 'file',
        type: file.type,
        getAsFile: () => file,
      })),
      types: ['Files'],
    },
  };
}
