import {DialogContent, MenuItem} from '@mui/material';
import {useForm} from 'react-hook-form';
import FormButtonGroup from '../../ui/FormButtonGroup';
import GridForm from '../../ui/GridForm';
import LabeledField from '../../ui/LabeledField';
import LabeledFileField from '../../ui/LabeledFileField';
import TagsCell from './TagsCell';
import {createRules} from './const/modalProperty';
import {useDispatch, useSelector} from 'react-redux';
import {
  createCertificate,
  updateCertificate,
} from '../../../store/certificates/certificateSlice';
import {selectCategories} from '../../../store/categories/categorySelector';
import {createFormData} from '../../../utils/createFormData';

const CertificateForm = ({certificate, onClose}) => {
  const dispatch = useDispatch();
  const categoryList = useSelector(selectCategories);
  const rules = createRules(certificate);

  const defaultValues = {
    name: certificate?.name || '',
    duration: certificate?.duration || '',
    category: certificate?.category || '',
    description: certificate?.description || '',
    price: certificate?.price || '',
    image: null,
    tags: certificate?.tags || [],
  };

  const {
    handleSubmit,
    control,
    reset,
    formState: {errors, isSubmitting},
  } = useForm({
    mode: 'onTouched',
    defaultValues,
  });

  const onSubmit = data => {
    const {image, tags, ...certificateData} = data;
    const formData = createFormData({
      ...certificateData,
    });

    if (image?.[0]) {
      formData.append('image', image[0]);
    }
    tags.forEach(tag => formData.append('tags', tag.name));

    if (certificate) {
      formData.append('id', certificate.giftCertificateId);
      dispatch(updateCertificate(formData));
    } else {
      dispatch(createCertificate(formData));
    }

    onClose();
  };

  return (
    <DialogContent sx={{p: 0}}>
      <GridForm
        gap="10px 70px"
        id="certificateForm"
        noValidate
        onSubmit={handleSubmit(onSubmit)}
        enctype="multipart/form-data">
        <LabeledField
          id="name"
          label="Title"
          control={control}
          rules={rules.name}
          error={!!errors.name?.message}
          helperText={errors.name?.message}
        />

        <LabeledField
          type="number"
          id="duration"
          label="Duration"
          inputProps={{min: '1'}}
          control={control}
          rules={rules.duration}
          error={!!errors.duration?.message}
          helperText={errors.duration?.message}
        />

        <LabeledField
          select
          id="category"
          label="Category"
          control={control}
          rules={rules.category}
          error={!!errors.category?.message}
          helperText={errors.category?.message}>
          {categoryList.map(category => (
            <MenuItem key={category} value={category}>
              {category}
            </MenuItem>
          ))}
        </LabeledField>

        <LabeledField
          fieldStyles={{gridRow: 'span 2'}}
          id="description"
          label="Description"
          control={control}
          rules={rules.description}
          multiline
          minRows={5}
          maxRows={5}
          error={!!errors.description?.message}
          helperText={errors.description?.message}
        />

        <LabeledField
          type="number"
          id="price"
          label="Price"
          inputProps={{min: '0.01', step: '0.01'}}
          control={control}
          rules={rules.price}
          error={!!errors.price?.message}
          helperText={errors.price?.message}
        />

        <LabeledFileField
          control={control}
          rules={rules.image}
          errors={errors}
        />

        <TagsCell
          sx={{gridRow: 'span 2'}}
          control={control}
          isSubmitting={isSubmitting}
        />

        <FormButtonGroup
          button1="Reset"
          onClick1={() => reset(defaultValues)}
          button2="Save"
          form="certificateForm"
          className="buttonPlace"
        />
      </GridForm>
    </DialogContent>
  );
};

export default CertificateForm;
