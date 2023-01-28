export const createTagsLine = tags =>
  tags.map(tag => `#${tag.name}`).join(', ');
